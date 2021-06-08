package cdm.base.math.functions;

import cdm.base.math.Vector;

public class NullVectorImpl extends NullVector{
    @Override
    protected Vector.VectorBuilder doEvaluate() {
        return Vector.builder();
    }
}
