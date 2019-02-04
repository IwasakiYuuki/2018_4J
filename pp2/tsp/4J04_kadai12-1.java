//4J04ä‚çËóIãIÅ@â€ëËî‘çÜÇPÇQÅ|ÇP
package tsp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

class ReadDataFromFile{

    private Path path;
    private List<String> lines;
    public int[][] datas;
    public int size;

    public ReadDataFromFile(String path){
        this.path = Paths.get(path);
        this._readDataFromFile();
        this.size = lines.size();
        this._getIntFromList();
    }

    private void _readDataFromFile(){
        try {
            this.lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _getIntFromList(){
        String[] buf;
        int i, j;
        datas = new int[this.lines.size()][this.lines.size()];
        buf = new String[this.lines.size()];
        for(i=0; i<this.lines.size(); i++){
            buf = this.lines.get(i).split(" ");
            for(j=0; j<buf.length; j++){
                datas[i][j] = Integer.parseInt(buf[j]);
            }
        }
    }
}

public class GreedyEquation extends ReadDataFromFile {

    public int minCostOfRoute;

    public GreedyEquation(){
        super("tsp/data/table.dat");
        this._greedyEquation();
    }

    public void _greedyEquation(){
        int i, minCost=-1, cost;
        for(i=0; i<this.size; i++){
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
        int route[] = new int[this.size];
        int minCost, buf=0, cur=startPoint, sumCost=0;

        for(i=0; i<this.size; i++){
            route[i] = 0;
        }
        route[startPoint] = -1;
        while(cnt < (this.size - 1)){
            minCost = -1;
            for(i=0; i<this.size; i++){
                if((route[i] != -1) && (this.datas[cur][i] != -1)){
                    if(minCost == -1){
                        minCost = this.datas[cur][i];
                        buf = i;
                    }else{
                        if(minCost > this.datas[cur][i]){
                            minCost = this.datas[cur][i];
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

    public static void main(String[] args){
        GreedyEquation ge = new GreedyEquation();
        System.out.println("ç≈íZåoòHÇÃÉRÉXÉgÅF"+String.valueOf(ge.minCostOfRoute));
    }
}

/*Å@é¿çsåãâ 
ç≈íZåoòHÇÃÉRÉXÉgÅF240
*/