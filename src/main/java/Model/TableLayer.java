package Model;

import java.util.ArrayList;
import java.util.List;

public class TableLayer {
    private List<TableLayer> pointsTo;
    private int tableNum;

    public TableLayer(int tableNum) {
        this.tableNum = tableNum;
        pointsTo = new ArrayList<>();
    }

    public TableLayer (TableLayer copy) {
        pointsTo = new ArrayList<>();
        for (TableLayer tl : copy.getPointsTo()) {
            pointsTo.add(new TableLayer(tl));
        }
        tableNum = copy.getTableNum();
    }

    public List<TableLayer> getPointsTo() {
        return pointsTo;
    }

    public void setPointsTo(List<TableLayer> pointsTo) {
        this.pointsTo = pointsTo;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public TableLayer addNewLayer (int tableNum) {
        TableLayer addedlayer = new TableLayer(tableNum);
        pointsTo.add(new TableLayer(tableNum));
        return addedlayer;
    }

    public List<TableLayer> getLastLayer (List<Integer> nodesActual) {
        if (pointsTo.size() != 0) {
            int i = 0;
            List<TableLayer> result = new ArrayList<>();
            for (TableLayer tl : pointsTo) {
                List<TableLayer> tableLayer = tl.getLastLayer(nodesActual);
                if (tableLayer != null) {
                    result.addAll(tableLayer);
                }
            }
            return result;
        } else {
            if (nodesActual.contains(tableNum)) {
                ArrayList<TableLayer> arrayList = new ArrayList<>();
                arrayList.add(this);
                return arrayList;
            } else {
                return null;
            }
        }
    }
}

