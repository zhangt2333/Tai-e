package sa.pta.analysis.heap;

import sa.pta.element.Obj;

public interface HeapModel {

    Obj getObj(Object allocationSite);
}
