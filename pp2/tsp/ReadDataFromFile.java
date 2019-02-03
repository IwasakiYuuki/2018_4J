package tsp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class ReadDataFromFile{

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

    public static void main(String[] args){
        ReadDataFromFile rd = new ReadDataFromFile("tsp/data/table.dat");
        int i, j;
        for(i=0; i<30; i++){
            for(j=0; j<10; j++){
                System.out.print(String.valueOf(rd.datas[i][j])+" ");
            }
            System.out.println(" ");
        }
    }
}
