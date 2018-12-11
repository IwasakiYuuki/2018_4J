//4J04岩崎悠紀　課題番号８
package pr2calc;

public class NonlinearEquation{

    public static final double EPSILON = 0.001;
    public static final int MAXIMUM_IT = 100;

    private double initialValue_;
    private double answer_;
    private int iteration_;

    // コンストラクタ(最低一つ用意せよ）
    public NonlinearEquation(double initialValue_){
            this.initialValue_ = initialValue_;
    }

    private void _solveNLEByLinearIteration(){
        double value,      // x_k に対応
               pastValue;  // x_{k-1} に対応（初回のpastValue = x_0とする）
        int i;

        // 初期反復解をvalueに設定し
        value = this.initialValue_;
        for(i=0; i<MAXIMUM_IT; i++){
            pastValue = value;
            value = Math.sqrt(14+pastValue);
            System.out.println("value = "+value+", pastValue = "+pastValue);
            if(Math.abs(value-pastValue)<EPSILON){
                break;
            }
        }
        this.answer_ = value;
        this.iteration_ = i;
        // |value - pastValue| が EPSILON 未満となる(近似解が見つかる)、もしくは
        // 繰り返し回数がMAXIMUM_IT 回に到達するまで繰り返し
        // 繰り返しで得られる反復解の途中経過を表示するようにすること
    }


    public static void main(String[] args) {
        NonlinearEquation eqn = new NonlinearEquation(2.0);

            // 初期反復解を設定
            eqn._solveNLEByLinearIteration();
            if(eqn.iteration_ < MAXIMUM_IT){
                System.out.println("x = "+eqn.answer_+" at iteration "+eqn.iteration_);
            }else{
                System.out.println("解が見つかりませんでした");
            }
            // 近似解が得られたら近似解＆何回目の繰り返しで解が得られたかを表示
            // 得られなければ、解が見つからなかったことを表示
    }
}