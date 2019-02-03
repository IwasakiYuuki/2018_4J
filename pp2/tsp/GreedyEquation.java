package tsp;

public class GreedyEquation{

    public int minCostOfRoute;
    private ReadDataFromFile rd;

    public GreedyEquation(){
        rd = new ReadDataFromFile("tsp/data/table.dat");
        this._greedyEquation();
    }

    public void _greedyEquation(){
        int i, minCost=-1, cost;
        for(i=0; i<rd.size; i++){
            cost = this._getCostUponStartPoint(i);
            if(minCost != -1){
                if(minCost > cost){
                    minCost = cost;
                }
            }else{
                minCost = cost;
            }
        }
        this.minCostOfRoute = minCost;
    }

    private int _getCostUponStartPoint(int startPoint){
        int i, cnt=0;
        int route[] = new int[rd.size];
        int minCost, buf=0, cur=startPoint, sumCost=0;
        int[][] datas = rd.datas;

        while(cnt < rd.size){
            minCost = -1;
            for(i=0; i<rd.size; i++){
                if((route[i] != -1) && (datas[cur][i] != -1)){
                    if(minCost == -1){
                        minCost = datas[cur][i];
                        buf = i;
                    }else{
                        if(minCost > datas[cur][i]){
                            minCost = datas[cur][i];
                            buf = i;
                        }
                    }
                }
            }
            cur = buf;
            sumCost += minCost;
            route[buf] = -1;
            cnt++;
        }
        return sumCost;
    }

    public static void main(String[] agrs){
        GreedyEquation ge = new GreedyEquation();
        System.out.println(String.valueOf(ge.minCostOfRoute));
    }
}