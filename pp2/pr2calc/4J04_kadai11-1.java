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

    private void _solveNLEByNewton(){
        double value,      // x_k に対応
               pastValue;  // x_{k-1} に対応（初回のpastValue = x_0とする）
        int i;

        // 初期反復解をvalueに設定し
        value = this.initialValue_;
        for(i=0; i<MAXIMUM_IT; i++){
            pastValue = value;
            value = value - (this._f_Newton(value)/this._f_dash(value));
            System.out.println("xNext = "+value+", f(xNext) = "+this._f_Newton(value));
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

    private double _f_Newton(double x){
        return (Math.pow(Math.E, x) - 2.97*x);
    }

    private double _f_dash(double x){
        double delta = 0.000001;
        return ((this._f_Newton(x+delta)-this._f_Newton(x))/delta);
    }

    public static void main(String[] args) {
        NonlinearEquation eqn = new NonlinearEquation(1.19);

            // 初期反復解を設定
            eqn._solveNLEByNewton();
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
xNext = 1.9696662347697322, f(xNext) = 1.3183748479506825
xNext = 1.6556393945015748, f(xNext) = 0.31917799297276694
xNext = 1.5148108458628855, f(xNext) = 0.049572454187647885
xNext = 1.483407312348921, f(xNext) = 0.0022196308575122004
xNext = 1.4818636954657036, f(xNext) = 5.252216539908261E-6
xNext = 1.4818600255194414, f(xNext) = 3.771560841414612E-11
x = 1.4818600255194414 at iteration 5
----------------------------*/
