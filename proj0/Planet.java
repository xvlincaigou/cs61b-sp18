public class Planet{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Planet(double xP,double yP,double xV,double yV,double m,String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p){
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }
    public double calcForceExertedBy(Planet p){
        return (6.67e-11 * mass * p.mass / calcDistance(p) / calcDistance(p));
    }
    public double calcForceExertedByX(Planet p){
        return (calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p));
    }
    public double calcForceExertedByY(Planet p){
        return (calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p));
    }
    public double calcNetForceExertedByX(Planet[] p){
        double ans = 0;
        for(int i = 0;i < p.length;++ i){
            if(!p[i].equals(this)){
                ans += this.calcForceExertedByX(p[i]);
            }
        }
        return ans;
    }
    public double calcNetForceExertedByY(Planet[] p){
        double ans = 0;
        for(int i = 0;i < p.length;++ i){
            if(!p[i].equals(this)){
                ans += this.calcForceExertedByY(p[i]);
            }
        }
        return ans;
    }
    public void update(double dt,double fx,double fy){
        double ax = fx / mass,ay = fy / mass;
        xxVel += ax * dt;
        yyVel += ay * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }
    public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}