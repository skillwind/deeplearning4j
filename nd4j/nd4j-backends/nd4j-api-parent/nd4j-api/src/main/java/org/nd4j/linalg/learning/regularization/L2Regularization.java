package org.nd4j.linalg.learning.regularization;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * L2 regularization: very similar to weight decay, but is applied before the updater is applied, not after.
 * <br>
 * <br>
 * Implements updating as follows:<br>
 * {@code w -= updater(gradient + l2 * w}<br>
 */
public class L2Regularization implements Regularization {

    protected final double l2;

    /**
     * @param l2   L2 regularization coefficient
     */
    public L2Regularization(double l2) {
        this.l2 = l2;
    }

    @Override
    public ApplyStep applyStep(){
        return ApplyStep.BEFORE_UPDATER;
    }

    @Override
    public void apply(INDArray param, INDArray gradView, double lr) {
        //L = loss + l2 * 0.5 * sum_i x[i]^2
        //dL/dx[i] = dloss/dx[i] + l2 * x[i]
        Nd4j.getBlasWrapper().level1().axpy(param.length(), l2, param, gradView);        //Gradient += scale * param
    }

    @Override
    public double score(INDArray param) {
        return l2 * param.norm2Number().doubleValue();
    }
}
