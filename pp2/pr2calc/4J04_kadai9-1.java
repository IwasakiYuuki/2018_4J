//4J04岩崎悠紀　課題番号９－１
package pr2calc;

public class NonlinearEquation{

    public static final double EPSILON = 0.001;
    public static final int MAXIMUM_IT = 100;
    public static final double NEGATIVE_MAX = 0.0;
    public static final double POSITIVE_MAX = 4.0;

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

    private void _solveNLEByBisectionMethod(){
        double value, pastValue, left, right, y;
        int i;

        value = 0.0;
        pastValue = 0.0;
        left = NEGATIVE_MAX;
        right = POSITIVE_MAX;

        for(i=0; i<MAXIMUM_IT; i++){
            value = (right - left)/2.0 + left;
            if((value + 2.0) == 0){
                y = 1.0;
            }else{
                y = Math.sin(value+2.0)/(value+2.0);
            }
            if(y > 0){
                left = value;
            }else if(y < 0){
                right = value;
            }
            System.out.printf("xMid = %f, f(xMid)=%f, xPastMid = %f\n", value, y, pastValue);
            if(Math.abs(y) < EPSILON){
                break;
            }
            pastValue = value;
        }
        this.answer_ = value;
        this.iteration_ = i;
    }


    public static void main(String[] args) {
        NonlinearEquation eqn = new NonlinearEquation(2.0);

            // 初期反復解を設定
            eqn._solveNLEByBisectionMethod();
            if(eqn.iteration_ < MAXIMUM_IT){
                System.out.println("x = "+eqn.answer_+" at iteration "+eqn.iteration_);
            }else{
                System.out.println("解が見つかりませんでした");
            }
            // 近似解が得られたら近似解＆何回目の繰り返しで解が得られたかを表示
            // 得られなければ、解が見つからなかったことを表示
    }
}

/*----------実行結果----------
 xMid = 2.000000, f(xMid)=-0.189201, xPastMid = 0.000000
 xMid = 1.000000, f(xMid)=0.047040, xPastMid = 2.000000
 xMid = 1.500000, f(xMid)=-0.100224, xPastMid = 1.000000
 xMid = 1.250000, f(xMid)=-0.033291, xPastMid = 1.500000
 xMid = 1.125000, f(xMid)=0.005309, xPastMid = 1.250000
 xMid = 1.187500, f(xMid)=-0.014397, xPastMid = 1.125000
 xMid = 1.156250, f(xMid)=-0.004644, xPastMid = 1.187500
 xMid = 1.140625, f(xMid)=0.000308, xPastMid = 1.156250
 x = 1.140625 at iteration 7
----------------------------*/
