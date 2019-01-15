//4J04岩崎悠紀　課題番号１０−２
package pr2calc;

public class NonlinearEquation{

    public static final double EPSILON = 0.001;
    public static final int MAXIMUM_IT = 100;
    public static final double NEGATIVE_MAX = 0.0;
    public static final double POSITIVE_MAX = 3.0;

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

    private double _f(double x){
        if((x + 3.0) == 0){
            return 1.0;
        }else{
            return Math.sin(x+3.0)/(x+3.0);
        }
    }

    private void _solveNLEByRegulaFalsi() {
        double value, pastValue, left, right, y;
        int i;

        value = 0.0;
        pastValue = 0.0;
        left = NEGATIVE_MAX;
        right = POSITIVE_MAX;

        for(i=0; i<MAXIMUM_IT; i++){
            value = (left * this._f(right) - right * this._f(left)) / (this._f(right) -  this._f(left));
            y = this._f(value);
            if(y > 0){
                left = value;
            }else if(y < 0){
                right = value;
            }
            System.out.printf("xNext = %f, f(xNext)=%f, xPastNext = %f\n", value, y, pastValue);
            if(Math.abs(value - pastValue) < EPSILON){
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
            eqn._solveNLEByRegulaFalsi();
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
xNext = 1.507543, f(xNext)=-0.217212, xPastNext = 0.000000
xNext = 0.268361, f(xNext)=-0.038683, xPastNext = 1.507543
xNext = 0.147262, f(xNext)=-0.001801, xPastNext = 0.268361
xNext = 0.141831, f(xNext)=-0.000076, xPastNext = 0.147262
xNext = 0.141603, f(xNext)=-0.000003, xPastNext = 0.141831
x = 0.14160262391110684 at iteration 4
----------------------------*/
