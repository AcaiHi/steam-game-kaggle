 


class f1 extends f_xj//Gold stein f(x)=3.0 @x=(0,-1) -2<x[i]<2 i=1,2
{
double func(double x[])
{
double first=0.0;double second=0.0;
first=(1.0+(x[0]+x[1]+1.0)*(x[0]+x[1]+1.0)*(19.0-14.0*x[0]+3.0*x[0]*x[0]-14.0*x[1]+6.0*x[0]*x[1]+3.0*x[1]*x[1]));	
second=30.0+(2.0*x[0]-3.0*x[1])*(2.0*x[0]-3.0*x[1])*(18.0-32.0*x[0]+12.0*x[0]*x[0]+48.0*x[1]-36.0*x[0]*x[1]+27*x[1]*x[1]);	
return first*second;	
}	
}

class f2 extends f_xj// Beale f(x)=0    @x=(3,0.5)   -4.5<x[i]<4.5, i = 1, 2.
{
double func(double x[])
{
double first=0.0;	
first=((1.5-x[0]+x[0]*x[1])*(1.5-x[0]+x[0]*x[1]))+((2.25-x[0]+x[0]*x[1]*x[1])*(2.25-x[0]+x[0]*x[1]*x[1]))+((2.625-x[0]+x[0]*x[1]*x[1]*x[1])*(2.625-x[0]+x[0]*x[1]*x[1]*x[1]));
return first;	
}	
}

class f3 extends f_xj// Bohachecsky 1 f(x)=0  @x=(0.0,0.0)   -5.0<x[i]<5.0, i = 1, 2.
{
double func(double x[])
{
double first=0.0;	
first=x[0]*x[0]+2.0*x[1]*x[1]-0.3*(Math.cos(Math.PI*3.0*x[0]))-0.4*Math.cos(4.0*Math.PI*x[1])+0.7;
return first;	
}	
}

class f4 extends f_xj// Bohachecsky 2 f(x)=0  @x=(0.0,0.0)   -5.0<x[i]<5.0, i = 1, 2.
{
double func(double x[])
{
double first=0.0;	
first=x[0]*x[0]+2.0*x[1]*x[1]-(0.3*(Math.cos(Math.PI*3.0*x[0]))*Math.cos(4.0*Math.PI*x[1]))+0.3;
return first;	
}	
}

class f5 extends f_xj// Bohachecsky 3 f(x)=0  @x=(0.0,0.0)   -5.0<x[i]<5.0, i = 1, 2.
{
double func(double x[])
{
double first=0.0;	
first=x[0]*x[0]+2.0*x[1]*x[1]-(0.3*(Math.cos(Math.PI*3.0*x[0]+Math.PI*4.0*x[1])))+0.3;
return first;	
}	
}

class f6 extends f_xj// Booth  f(x)=0  @x=(1.0,3.0)   -10.0<x[i]<10.0, i = 1, 2.
{
double func(double x[])
{
double first=0.0;	
first=(x[0]+2.0*x[1]-7.0)*(x[0]+2.0*x[1]-7.0)+(2.0*x[0]+x[1]-5.0)*(2.0*x[0]+x[1]-5.0);
return first;	
}	
}

class f7 extends f_xj// Branin  f(x)=0.397887  @x=(-pi,12.275),(pi,2.275),(9.42478,2.475)   -5.0<=x[0]<=10.0, 0.0<=x[1]<=15.0
{
double func(double x[])
{
double first=0.0;	
first=((x[1]-(5.1*x[0]*x[0]/(4.0*Math.PI*Math.PI))+(5.0*x[0]/Math.PI)-6.0)*(x[1]-(5.1*x[0]*x[0]/(4.0*Math.PI*Math.PI))+(5.0*x[0]/Math.PI)-6.0))+(10.0*(1.0-(1.0/(8.0*3.1415)))*Math.cos(x[0]))+10.0;
return first;	
}	
}

class f8 extends f_xj// Colville  f(x)=0.0  @x=(1,1,1,1)   -10.0<=x[i]<=10.0 i=0,1,2,3
{
double func(double x[])
{
double first=0.0;	
first=(100.0*(x[0]-x[1]*x[1])*(x[0]-x[1]*x[1]))+((1.0-x[0])*(1.0-x[0]))+(90.0*(x[3]-x[2]*x[2])*(x[3]-x[2]*x[2]))+((1.0-x[2])*(1.0-x[2]))+(10.1*((x[1]-1.0)*(x[1]-1.0)+(x[3]-1.0)*(x[3]-1.0)))+(19.8*(x[1]-1.0)*(x[3]-1.0));
return first;	
}	
}

class f9 extends f_xj// Easom  f(x)=-1.0  @x=(pi,pi)   -100.0<=x[i]<=100.0 i=0,1
{
double func(double x[])
{
double first=0.0;	
first=-Math.cos(x[0])*Math.cos(x[1])*Math.exp(-(x[0]-Math.PI)*(x[0]-Math.PI)-(x[1]-Math.PI)*(x[1]-Math.PI));
return first;	
}	
}

class f10 extends f_xj// Himmelblau f(x)=0.0  @x=(3.0,2.0),(-2.8051,3.1313),(-3.7793,-3.2831),(3.5844,-1.8481)   -6.0<=x[i]<=6.0 i=0,1
{
double func(double x[])
{
double first=0.0;	
first=(((x[0]*x[0]+x[1]-11.0)*(x[0]*x[0]+x[1]-11.0))+(x[0]+x[1]*x[1]-7.0)*(x[0]+x[1]*x[1]-7.0));
return first;	
}	
}

class f11 extends f_xj// Griewank f(x)=0.0  @x=(0,0)<---global minima     several local minimas      -600<x[i]<600 i=1,2,.. x.length 
{
double func(double x[])
{
   double s=0.0;
   double fact=1.0;
   int m=x.length;
   for(int i=0;i<m;i++)
   {s+=x[i]*x[i];}	
   for(int i=0;i<m;i++)
   {fact*=Math.cos(x[i]/Math.sqrt(i+1));}
   return (s/4000.0)+1.0+(-fact);	
}	
}

class f12 extends f_xj// Hartman3 f(x)=-3.86  @x=(0.114,0.556,0.852)   0.0<x[i]<1.0 for n=3 variable 
{
double func(double x[])
{
double[][] A={{3.0,10.0,30.0},{0.1,10.0,35.0},{3.0,10.0,30.0},{0.1,10.0,35.0}};
double c[]={1.0,1.2,3.0,3.2};
double p[][]={{0.3689,0.1170,0.2673},{0.4699,0.4387,0.7470},{0.1091,0.8732,0.5547},{0.03815,0.5743,0.8828}}   ;
double sin;
double sout=0.0;
    for(int i=0;i<=3;i++)	
    {  sin=0.0;
	    for(int j=0;j<=3;)
	   {sin+=A[i][j]*(x[j]-p[i][j])*(x[j]-p[i][j]);} 
	   sout+=c[i]*Math.exp(-sin);}	
	
return -sout;	
}	
}

class f13 extends f_xj// Matyas function f(x)=0.0 @x(0,0)  -10.0<=x[i]<=10.0
{
double func(double x[])
{
return 0.26*(x[0]*x[0]+x[1]*x[1])-0.48*x[0]*x[1];
}	
}

class f14 extends f_xj //Michalewicz n=x.length=2 f(x)=-1.8013    0<=x[i]<=pi
{                                        //    =5 f(x)=-4.687658        
   double func(double x[])               //   =10 f(x)=-9.66015  
   {
	int n=x.length;   
	double m=10.0;
	double s=0.0;
	for(int i=0;i<n;i++)
	{s+=Math.sin(x[i])*Math.pow(Math.sin(((double)i+1.0)*x[i]*x[i]/3.1415),2.0*m);}   
	return -s;   
   }	
}

class f15 extends f_xj //Perm function!!!!!! error  
{                                               
   double func(double x[])                
   {
	int n=x.length;   
	double b=0.5;

	double sin;
	double sout=0.0;
	for(int k=0;k<n;k++)
	{sin=0.0;
	 for(int i=0;i<n;i++)
	 {sin+=(Math.pow((double)i,(double)k)+b)*(Math.pow((x[i]/(double)i),(double)k)-1.0);}	
	 sout+=sin*sin;	
	}
	return sout;   
   }	
}

class f16 extends f_xj //Powell function !!Error     Must--->mod(x.length,4)==0 
{                                               
   double func(double x[])                
   {
	int n=x.length;
	int m=n/4;double s=0.0;
	for(int j=1;j<=m;j++)
	{s+=((x[4*j-4]+10.0*x[4*j-3])*(x[4*j-4]+10.0*x[4*j-3]))+(Math.sqrt(5.0)*(x[4*j-2]-x[4*j-1])*(x[4*j-2]-x[4*j-1]))+(Math.pow((x[4*j-3]-2.0*x[4*j-2]),4.0))+(Math.sqrt(10.0)*Math.pow(x[4*j-4]-x[4*j-1],4.0));}
	return s;
	   
   }
}

class f17 extends f_xj //Shekel function     f(x)=-10.1532 m=5; @x=(4,4,4,4) 0<=x[i]<=10.0   
{                                          //f(x)=-10.4029 m=7;     
   double func(double x[])                 //f(x)=-10.5364 m=10;
   {
	int n=x.length;
	double A[][]={{4.0,4.0,4.0,4.0},{1.0,1.0,1.0,1.0},{8.0,8.0,8.0,8.0},{6.0,6.0,6.0,6.0},{3.0,7.0,3.0,7.0},{2.0,9.0,2.0,9.0},{5.0,5.0,3.0,3.0},{8.0,1.0,8.0,1.0},{6.0,2.0,6.0,2.0},{7.0,3.6,7.0,3.6}};
	double c[]={0.1,0.2,0.2,0.4,0.4,0.6,0.3,0.7,0.5,0.5};
	double sin=0.0;
	double sout=0.0;
	for(int i=0;i<10;i++)
	{ sin=c[i];
	  for(int j=0;j<n;j++)	
	  {sin+=(x[j]-A[i][j])*(x[j]-A[i][j]);}
	  sout+=(1.0/sin);	
	}
	return -sout;
	}
}

class f18 extends f_xj //Trid function     f(x)=-50.0 x.length=6;  -x.length^2<=x[i]<=x.length^2   
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;double s2=0.0;
	for(int i=0;i<n;i++)
	{s1+=Math.pow(x[i]-1.0,2.0);}
	for(int i=1;i<n;i++)
	{s2+=x[i]*x[i-1];}
	return s1-s2;
	}
}

class f19 extends f_xj //Zakharov function     f(x)=0.0 @x=(0,0,..)   -5<=x[i]<=10   
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;double s2=0.0;double s3=0.0;
	for(int i=0;i<n;i++)
	{s1+=x[i]*x[i];}
	for(int i=0;i<n;i++)
	{s2+=0.5*(double)i*x[i];}
	return s1+Math.pow(s2,2.0)+Math.pow(s2,4.0);
	
	}
}

class f20 extends f_xj //Levy function         f(x)=0   @x=(1,1,1...) -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double z[]=new double[n];
	for(int i=0;i<n;i++)
	{z[i]=1.0+((x[i]-1.0)/4.0);}
	double s=Math.pow(Math.sin(3.1415*z[0]),2.0);
	for(int i=0;i<n-1;i++)
	{s+=Math.pow((z[i]-1.0),2.0)*(1.0+10.0*Math.pow(Math.sin(3.1415*z[i]+1.0),2.0));}
	return s+Math.pow(z[n-1]-1.0,2.0)*(Math.pow(Math.sin(2.0*3.1415*z[n-1]),2.0)+1.0);
   }
}


class f21 extends f_xj //Dixon price function         f(x)=0    -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
	for(int i=1;i<n;i++)
	{s1+=((double)i+1.0)*(2.0*x[i]*x[i]-x[i-1])*(2.0*x[i]*x[i]-x[i-1]);}
	return s1+(x[0]-1.0)*(x[0]-1.0);
	}
}

class f22 extends f_xj //Salomon's function         f(x)=0      @x=(0,0,0...) -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
    for(int i=0;i<n;i++)
    {s1+=x[i]*x[i];}
    s1=Math.sqrt(s1);
	return -Math.cos(2.0*3.1415*s1)+0.1*s1+1.0;	
   }
}

class f23 extends f_xj //Whitley's function         f(x)=0      @x=(0,0,0...) -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
    for(int i=0;i<n;i++)
    {
	  for(int j=0;j<n;j++)
	  {s1+=(Math.pow((100.0*(x[i]*x[i]-x[j])*(x[i]*x[i]-x[j])+(1.0-x[j])*(1.0-x[j])),2.0)/4000.0)-Math.cos((100.0*(x[i]*x[i]-x[j])*(x[i]*x[i]-x[j])+(1.0-x[j])*(1.0-x[j])))+1.0;}    
	}
	return s1;
  }
}

class f24 extends f_xj //quartic function         f(x)=0      @x=(0,0,0...) -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
    for(int i=0;i<n;i++)
    {s1+=((double)i+1.0)*Math.pow(x[i],4.0);}
    s1+=Math.random();
	return s1;
  }
}

class f25 extends f_xj //quartic function         f(x)=0      @x=(0,0,0...) -10<x[i]<10 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
    for(int i=0;i<n;i++)
    {s1+=((double)i+1.0)*Math.pow(x[i],4.0);}
    s1+=Math.random();
	return s1;
  }
}

class f26 extends f_xj //Camel Back -6 Hump function         f(x)=-1.0316285       -5<x[i]<5 
{                                              
   double func(double x[])                 
   {
	int n=x.length;
    double s1=4.0*x[0]*x[0]-2.1*x[0]*x[0]*x[0]*x[0]+(x[0]*x[0]*x[0]*x[0]*x[0]*x[0]/3.0)+(x[0]*x[1])-4.0*x[1]*x[1]+4.0*x[1]*x[1]*x[1]*x[1];
    return s1;
   }
}

class f27 extends f_xj //Schwefel 2.22         f(x)=0    @x=(0,0,...)  -100<x[i]<100  
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
	double f1=1.0;
    for(int i=0;i<n;i++)
    {s1+=Math.abs(x[i]);f1*=Math.abs(x[i]);}
    return s1+f1;
   }
}

class f28 extends f_xj //Kowalik         f(x)=3.0748e-4    @x=(0,0,...)  -5<x[i]<5  
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double a[]={0.1957,0.1947,0.1735,0.16,0.0844,0.0627,0.0456,0.0342,0.0323,0.0235,0.0246};
    double b[]={0.25, 0.5, 1.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0};
    double s1=0.0;
    for(int i=0;i<11;i++)
    {s1+=Math.pow((a[i]-((x[0]*(1.0+x[1]*b[i]))/(1.0+x[2]*b[i]+x[3]*b[i]*b[i]))),2.0);              }
    return s1;
   }
}

class f29 extends f_xj //Schaffer function         f(x)=0    @x=(0,0,...)  -100<x[i]<100  
{                                              
   double func(double x[])                 
   {
	int n=x.length;
	double s1=0.0;
	for(int i=0;i<n;i++)
	{s1+=x[i]*x[i];}
	double s2=Math.sqrt(s1);
	return 0.5+((Math.pow(Math.sin(s1),2.0)-0.5)/(1.0+0.001*s1));
	}
}


class f30 extends f_xj  // Rosenbrock's valley     f(x)=0.0     -2.048<x[i]<2.048
{
double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;
double ff=0.0;
for(int i=0;i<n-1;i++)
{ff+=(100.0*(x[i+1]-x[i]*x[i])*(x[i+1]-x[i]*x[i])+(1.0-x[i])*(1.0-x[i]));}
return ff; 
}
}

class f31 extends f_xj // De Jong’s first function     f(x)=0  @x=(0,0,...)     -5.12<x[i]<5.12
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double ff=0;
int n=x.length;
for(int i=0;i<n;i++)
{ff+=x[i]*x[i];}
return ff;
}
} 

class f32 extends f_xj //Axis parallel hyper-ellipsoid 2.2    f(x)=0  @x=(0,0,...)     -5.12<x[i]<5.12
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double ff=0;
int n=x.length;

for(int i=0;i<n;i++)
{ff+=i*x[i]*x[i];}
return ff;
}
}

class f33 extends f_xj //Rotated hyper-ellipsoid function  -65.536<x[i]<65.536  f(x)=0   @x=(0,0,...)
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double ff=0;
int n=x.length;

for(int i=0;i<n;i++)
{
  for(int j=0;j<i;j++)
  {ff+=x[j]*x[j];}	
}
return ff;
}
}

class f34 extends f_xj //Rastrigin’s function 2.5        f(x)=0  @x=(0,0,...)     -5.12<x[i]<5.12
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double ff=0;
int n=x.length;
for(int i=0;i<n;i++)
{
ff+=x[i]*x[i]-10*Math.cos(2.0*Math.PI*x[i]);
}
return ff+10*n;
}
}

class f35 extends f_xj //sum of a different power function 2.8       f(x)=0   @x=(0,0,...)      -1<x[i]<1
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double ff=0;
int n=x.length;
double top=0;
for(int i=0;i<n;i++)
{top+=Math.pow(Math.abs(x[i]),(i+2));}
return top;
}
} 

class f36 extends f_xj //Ackley’s function 2.9        f(x)=0;      @x=(0,0,0...)     -32.768<x[i]<32.768
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double a=20.0;
double b=0.2;
double c=2.*Math.PI;

int n=x.length;

double r=Math.PI/180;
double top1=0.0;
for(int i=0;i<n;i++)
{top1+=x[i]*x[i];}
top1=Math.sqrt(top1/n);
double top2=0.0;
for(int i=0;i<n;i++)
{top2+=Math.cos(r*c*x[i]);}
top2=top2/n;
double top=-a*Math.exp(-b*top1)-Math.exp(top2)+a+Math.exp(1);
return top;
}
}

class f37 extends f_xj //“Drop wave” function ++        i=1,2...    f(x)=-1.0  @x=(0,0)   -5.12<x[i]<5.12
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double r=Math.PI/180;
double top1=-(1+Math.cos((12*Math.sqrt(x[0]*x[0]+x[1]*x[1]))))/(0.5*(x[0]*x[0]+x[1]*x[1])+2.0);
return top1;
}
}

class f38 extends f_xj //Shubert’s function   -10<x[i]<10   18 global minima
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	

double r=Math.PI/180;
double top1=0;
double top2=0;
for(int i=1;i<=5;i++)     
{top1+=i*Math.cos((i+1)*x[0]+1);}
for(int i=1;i<=5;i++)     
{top2+=i*Math.cos((i+1)*x[1]+1);}
return -top1*top2;
}
}

class f39 extends f_xj //Fletcher and Powell function---> min(alfa)<x[i]<max(alfa)  f(x)=0 @x=(alfa[1],alfa[2],,,alfa[i]) 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double a[][]={{-79.0, 56.0, -62.0, -9.0, 92.0, 48.0, -22.0, -34.0, -39.0, -40.0, -95.0, -69.0, -20.0, -66.0, -98.0, -66.0,-67.0, 37.0,-83.0, -45.0},{91.0, -9.0, -18.0, -59.0, 99.0, -45.0, 88.0, -14.0, -29.0, 26.0, 71.0, -65.0, 19.0, 45.0, 88.0, 18.0, -11.0, -81.0, -10.0, 42.0},{-38.0, 8.0, -12.0, -73.0, 40.0, 26.0, -64.0, 29.0, -82.0, -32.0, -89.0, -3.0, 88.0, 98.0, 53.0, 58.0, 45.0, -39.0, 34.0, -23.0},{-78.0,-18.0 -49.0, 65.0, 66.0, -40.0, 88.0, -95.0, -57.0, 10.0, -98.0, -11.0, -16.0, -55.0, 33.0, 84.0, 21.0, -43.0, 45.0, 100.0,},{-1.0, -43.0, 93.0, -18.0, -76.0, -68.0, -42.0, 22.0, 46.0, -14.0, 69.0, 27.0, -12.0, -26.0, 57.0, -13.0, 0.0, 1.0, 56.0, 17.0},{34.0, -96.0, 26.0, -56.0, -36.0, -85.0, -62.0, 13.0, 93.0, 78.0, -43.0, 96.0, 77.0, 65.0, -34.0, -52.0, 82.0, 18.0, -59.0, -55.0},{52.0, -46.0, -69.0, 99.0, -47.0, -72.0, -11.0, 55.0, -55.0, 91.0, -30.0, 7.0, -35.0, 23.0, -20.0, 55.0, 61.0, -39.0, -58.0, 13.0},{81.0, 47.0, 35.0, 55.0, 67.0, -13.0, 33.0, 14.0, 83.0, -42.0, 8.0, -45.0, -44.0, 12.0, 100.0, -9.0,-33.0, -11.0, 21.0, 14.0},{5.0, -43.0, -45.0, 46.0, 56.0, -94.0, -62.0, 52.0, 66.0, 55.0, -86.0, -29.0, -52.0, -71.0, -91.0, -46.0, 27.0, -27.0, 6.0, 67.0},{-50.0, 66.0, -47.0, -75.0, 89.0, -16.0, 82.0, 6.0, -85.0, -62.0, -30.0, 31.0, -7.0, -75.0, -26.0, -24.0, 46.0, -95.0, -71.0, -57.0},{24.0, 98.0, -50.0, 68.0, -97.0, -64.0, -24.0, 81.0, -59.0, -7.0, 85.0, -92.0, 2.0, 61.0, 52.0, -59.0, -91.0, 74.0, -99.0, -95.0},{-30.0, -63.0, -32.0, -90.0, -35.0, 44.0, -64.0, 57.0, 27.0, 87.0, -70.0, -39.0, -18.0, -89.0, 99.0, 40.0, 14.0, -58.0 ,-5.0, -42.0},{56.0, 3.0, 88.0, 38.0, -14.0, -15.0, 84.0, -9.0, 65.0, -20.0, -75.0, -37.0, 74.0, 66.0, -44.0, 72.0, 74.0, 90.0, -83.0, -40.0},{84.0, 1.0, 73.0, 43.0, 84.0, -99.0, -35.0, 24.0, -78.0, -58.0, 47.0, -83.0, 94.0, -86.0, -65.0, 63.0, -22.0, 65.0, 50.0, -40.0},{-21.0, -8.0, -48.0, 68.0, -91.0, 17.0, -52.0, -99.0, -23.0, 43.0, -8.0, -5.0, -98.0, -17.0, -62.0, -79.0, 60.0, -18.0, 54.0, 74.0},{35.0, 93.0, -98.0, -88.0, -8.0, 64.0, 15.0, 69.0, -65.0, -86.0, 58.0, -44.0, -9.0, -94.0, 68.0, -27.0, -79.0, -67.0, -35.0, -56.0},{-91.0, 73.0, 51.0, 68.0, 96.0, 49.0, 10.0, -13.0, -6.0, -23.0, 50.0, -89.0, 19.0, -67.0, 36.0, -97.0, 0.0, 3.0, 1.0, 39.0},{53.0, 66.0, 23.0, 10.0, -33.0, 62.0, -73.0, 22.0, -65.0, 37.0, -83.0, -65.0, 59.0, -51.0, -56.0, 98.0,-57.0, -11.0, -48.0, 88.0},{83.0, 48.0, 67.0, 27.0, 91.0, -33.0, -90.0, -34.0, 39.0, -36.0, -68.0, 17.0, -7.0, 14.0, 11.0, -10.0, 96.0, 98.0, -32.0, 56.0},{52.0, -52.0, -5.0, 19.0, -25.0, 15.0, -1.0, -11.0, 8.0, -70.0, -4.0, -7.0, -4.0, -6.0, 48.0, 88.0, 13.0, -56.0, 85.0, -65.0}};
double b[][]={{-65.0, -11.0, 76.0, 78.0, 30.0, 93.0, -86.0, -99.0, -37.0, 52.0, -20.0, -10.0, -97.0, -71.0, 16.0, 9.0, -99.0, -84.0, 90.0, -18.0, -94.0},
{59.0, 67.0, 49.0, -45.0, 52.0, -33.0, -34.0, 29.0, -39.0, -80.0, 22.0, 7.0, 3.0, -19.0, -15.0, 7.0, -83.0, -4.0, 84.0 -60.0, -4.0},
{21.0, -23.0, -80.0, 86.0, 86.0, -30.0, 39.0, -73.0, -91.0, 5.0, 83.0, -2.0, -45.0, -54.0, -81.0, -8.0, 14.0, 83.0, 73.0, 45.0, 32.0},
{-91.0, -75.0, 20.0, -64.0, -15.0, 17.0, -89.0, 36.0, -49.0, -2.0, 56.0, -6.0, 76.0, 56.0, 2.0, -68.0, -59.0, -70.0, 48.0, 2.0, 24.0},
{-79.0, 99.0, -31.0, -8.0, -67.0, -72.0, -43.0, -55.0, 76.0, -57.0, 1.0, -58.0, 3.0,-59.0, 30.0, 32.0, 57.0, 29.0, 66.0, 50.0, -80.0},
{-89.0, -35.0, -55.0, 75.0, 15.0, -6.0, -53.0, -56.0, -96.0, 87.0, -90.0, -93.0, 52.0, -86.0, -38.0, -55.0, -53.0, 94.0, 98.0, 4.0,-79.0},
{-76.0, 45.0, 74.0, 12.0, -12.0, -69.0, 2.0, 71.0, 75.0, -60.0, -50.0, 23.0, 0.0, 6.0, 44.0, -82.0, 37.0, 91.0, 84.0, -15.0, -63.0},
{-50.0, -88.0, 93.0, 68.0, 10.0, -13.0, 84.0, -21.0, 65.0, 14.0, 4.0, 92.0, 11.0, 67.0, -18.0, -51.0, 4.0, 21.0, -38.0, 75.0, -59.0},
{-23.0, -95.0, 99.0, 62.0, -37.0, 96.0, 27.0, 69.0, -64.0,-92.0, -12.0, 87.0, 93.0, -19.0, -99.0, -92.0, -34.0, -77.0, 17.0, -72.0, 29.0},
{-5.0, -57.0, -30.0, -6.0, -96.0, 75.0, 25.0, -6.0, 96.0, 77.0, -35.0, -10.0, 82.0, 82.0, 97.0, -39.0, -65.0, -8.0, 34.0, 72.0, 65.0},
{85.0, -9.0, -14.0, 27.0, -45.0, 70.0, 55.0, 26.0, -87.0, -98.0, -25.0, -12.0, 60.0, -45.0, -24.0, -42.0, -88.0, -46.0, -95.0, 53.0, 28.0},
{80.0, -47.0, 38.0, -6.0, 43.0, -59.0, 91.0, -41.0, 90.0, -63.0, 11.0, -54.0, 33.0, -61.0, 74.0, 96.0, 21.0, -77.0, -58.0, -75.0, -9.0},
{-66.0, -98.0, -4.0,96.0, -11.0, 88.0, -99.0, 5.0, 5.0, 58.0, -53.0, 52.0, -98.0, -97.0, 50.0, 49.0, 97.0, -62.0, 79.0, -10.0, -80.0},
{80.0, -95.0, 82.0, 5.0, -68.0, -54.0, 64.0, -2.0, 5.0, 10.0, 85.0, -33.0, -54.0, -30.0, -65.0, 58.0, 40.0, -21.0, -84.0, -66.0, -11.0},
{94.0, 85.0, -31.0, 37.0, -25.0, 60.0, 55.0, -13.0, 48.0, -23.0, -50.0, 84.0, -71.0, 54.0, 47.0, 18.0, -67.0, -30.0, 5.0, -46.0, 53.0},
{-29.0, 54.0, -10.0, -68.0, -54.0, -24.0, -16.0, 21.0, 32.0, 33.0, -27.0, 48.0, 37.0, -61.0, 97.0, 45.0, -90.0, 87.0, -95.0, 85.0, 67.0},
{76.0, -11.0, -48.0, 38.0, -7.0, 86.0, -55.0, 51.0, 26.0, 8.0, -96.0, 99.0, 69.0, -84.0, 41.0, 78.0, -53.0, 4.0, 29.0, 38.0, 16.0},
{-8.0, 48.0, 95.0, 47.0, 39.0, -11.0, -72.0, -95.0, -17.0, 33.0, 65.0, 96.0, -52.0, -17.0, -22.0, -15.0, -91.0, -41.0, -16.0, 23.0, 14.0},
{92.0, 87.0, 63.0, -63.0, -80.0, 96.0, -62.0, 71.0, -58.0, 17.0, -89.0, -35.0, -96.0, -79.0, 7.0, 46.0, -74.0, 88.0, 93.0, -44.0, 52.0},
{-21.0, 35.0, 16.0, -17.0, 54.0, -22.0, -93.0, 27.0, 88.0, 0.0, -67.0, 94.0, -24.0, -30.0, -90.0, -5.0, -48.0, 45.0, -90.0, 32.0, -81.0},
{-86.0, 31.0, -80.0, -79.0, -5.0, 11.0, -20.0, 9.0, 52.0, -38.0, 67.0, 64.0, -49.0, 23.0, -86.0, 39.0, -97.0, 76.0, 10.0, 81.0, 20.0}};


double alfa[]={-2.7910,2.5623,-1.0429,0.5097,-2.8096,1.1883,2.0771,-2.9926,0.0715,0.4142,-2.5010,1.7731,1.6473,0.4934,2.1038,-1.9930,0.3813,-2.2144,-2.5572,2.9449};
int n=x.length;
double A[]=new double[x.length];
double B[]=new double[x.length];
double s1=0.0;
double s2=0.0;
for(int i=0;i<n;i++)
{s1=0.0;   
   for(int j=0;j<n;j++)
   {s1+=a[i][j]*Math.sin(alfa[j])+b[i][j]*Math.cos(alfa[j]);}
 A[i]=s1;
}    
for(int i=0;i<n;i++)
{s2=0.0;   
   for(int j=0;j<n;j++)
   {s2+=a[i][j]*Math.sin(x[j])+b[i][j]*Math.cos(x[j]);}
 B[i]=s2;
}
double s3=0.0;
for(int i=0;i<n;i++)
{s3+=(A[i]-B[i])*(A[i]-B[i]);}
return s3;
}
}


class f40 extends f_xj //Step function   f(x)=0   @x(-0.5,-0.5)   -5.0<x[i]<5.0
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
for(int i=0;i<n;i++)
{s1+=Math.pow(Math.abs(x[i]+0.5),2.0);}
return s1;
}
}



class f41 extends f_xj //Penalized function   f(x)=0   @x(-1.0,-1.0)   -5.0<x[i]<5.0
{
public double u(double x,double a,double k,double m)
{
   double c=0.0;	
   	
     if(x>a){c=k*Math.pow(x-a,m);return c;}	
	  if((x>=-a)||(x<=a)){c=0;return c;}
	  if(x<-a){c=k*Math.pow(-x-a,m);return c;}
   
   return c;	
}	

public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
double[] y=new double[n];
for(int i=0;i<n;i++)
{y[i]=(0.25*(x[i]+1.0))+1.0;}
s1=Math.pow(Math.sin(3.1415*y[0]),2.0)*10.0;

for(int i=0;i<n-1;i++)
{s1+=Math.pow((y[i]-1.0),2.0)*(10.0*Math.pow(Math.sin(3.1415*y[i+1]),2.0)+1.0);}
s1+=Math.pow((y[n-1]-1.0),2.0);
s1=s1*3.1415/(double)n;
double s2=0.0;
for(int i=0;i<n;i++)
{s2+=u(x[i],10.0,100.0,4.0);}
return s1+s2;
}
}

class f42 extends f_xj //Penalized2 function   f(x)=0   @x(1.0,1.0,....)   -5.0<x[i]<5.0
{
public double u(double x,double a,double k,double m)
{
   double c=0.0;	
   	
     if(x>a){c=k*Math.pow(x-a,m);return c;}	
	  if((x>=-a)||(x<=a)){c=0;return c;}
	  if(x<-a){c=k*Math.pow(-x-a,m);return c;}
   
   return c;	
}	

public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;

s1=Math.pow(Math.sin(3.1415*x[0]),2.0);

for(int i=0;i<n-1;i++)
{s1+=Math.pow((x[i]-1.0),2.0)*(Math.pow(Math.sin(3.0*3.1415*x[i+1]),2.0)+1.0);}
s1+=Math.pow((x[n-1]-1.0),2.0)*(Math.pow(Math.sin(2.0*3.1415*x[n-1]),2.0)+1.0);
s1=s1*0.1;
double s2=0.0;
for(int i=0;i<n;i++)
{s2+=u(x[i],5.0,100.0,4.0);}
return s1+s2;
}
}

class f43 extends f_xj //Shekel's Foxholes
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;

double[][] a={{-32.0,-16.0,0.0,16.0,32.0,-32.0,-16.0,0.0,16.0,32.0,-32.0,-16.0,0.0,16.0,32.0,-32.0,-16.0,0.0,16.0,32.0,-32.0,-16.0,0.0,16.0,32.0},{-32.0,-32.0,-32.0,-32.0,-32.0,-16.0,-16.0,-16.0,-16.0,-16.0,0.0,0.0,0.0,0.0,0.0,16.0,16.0,16.0,16.0,16.0,32.0,32.0,32.0,32.0,32.0}};


double sin=0.0;double sout=0.0;
for(int j=0;j<25;j++)
{   sin=0.0;
    for(int i=0;i<2;i++)
    {sin+=Math.pow((x[i]-a[i][j]),6.0);}	
	sout+=1.0/((double)j+sin);
}
return 1.0/(0.002+sout);
}
}

class f44 extends f_xj //Shekel's Foxholes
{
public double func(double x[]) 
{
double a[][] = {
	{9.681, 0.667, 4.783, 9.095, 3.517, 9.325, 6.544, 0.211, 5.122, 2.020},
	{9.400, 2.041, 3.788, 7.931, 2.882, 2.672, 3.568, 1.284, 7.033, 7.374},
	{8.025, 9.152, 5.114, 7.621, 4.564, 4.711, 2.996, 6.126, 0.734, 4.982},
	{2.196, 0.415, 5.649, 6.979, 9.510, 9.166, 6.304, 6.054, 9.377, 1.426},
	{8.074, 8.777, 3.467, 1.863, 6.708, 6.349, 4.534, 0.276, 7.633, 1.567},
	{7.650, 5.658, 0.720, 2.764, 3.278, 5.283, 7.474, 6.274, 1.409, 8.208},
	{1.256, 3.605, 8.623, 6.905, 4.584, 8.133, 6.071, 6.888, 4.187, 5.448},
	{8.314, 2.261, 4.224, 1.781, 4.124, 0.932, 8.129, 8.658, 1.208, 5.762},
	{0.226, 8.858, 1.420, 0.945, 1.622, 4.698, 6.228, 9.096, 0.972, 7.637},
	{7.305, 2.228, 1.242, 5.928, 9.133, 1.826, 4.060, 5.204, 8.713, 8.247},
	{0.652, 7.027, 0.508, 4.876, 8.807, 4.632, 5.808, 6.937, 3.291, 7.016},
	{2.699, 3.516, 5.874, 4.119, 4.461, 7.496, 8.817, 0.690, 6.593, 9.789},
	{8.327, 3.897, 2.017, 9.570, 9.825, 1.150, 1.395, 3.885, 6.354, 0.109},
	{2.132, 7.006, 7.136, 2.641, 1.882, 5.943, 7.273, 7.691, 2.880, 0.564},
	{4.707, 5.579, 4.080, 0.581, 9.698, 8.542, 8.077, 8.515, 9.231, 4.670},
	{8.304, 7.559, 8.567, 0.322, 7.128, 8.392, 1.472, 8.524, 2.277, 7.826},
	{8.632, 4.409, 4.832, 5.768, 7.050, 6.715, 1.711, 4.323, 4.405, 4.591},
	{4.887, 9.112, 0.170, 8.967, 9.693, 9.867, 7.508, 7.770, 8.382, 6.740},
	{2.440, 6.686, 4.299, 1.007, 7.008, 1.427, 9.398, 8.480, 9.950, 1.675},
	{6.306, 8.583, 6.084, 1.138, 4.350, 3.134, 7.853, 6.061, 7.457, 2.258},
	{0.652, 2.343, 1.370, 0.821, 1.310, 1.063, 0.689, 8.819, 8.833, 9.070},
	{5.558, 1.272, 5.756, 9.857, 2.279, 2.764, 1.284, 1.677, 1.244, 1.234},
	{3.352, 7.549, 9.817, 9.437, 8.687, 4.167, 2.570, 6.540, 0.228, 0.027},
	{8.798, 0.880, 2.370, 0.168, 1.701, 3.680, 1.231, 2.390, 2.499, 0.064},
	{1.460, 8.057, 1.336, 7.217, 7.914, 3.615, 9.981, 9.198, 5.292, 1.224},
	{0.432, 8.645, 8.774, 0.249, 8.081, 7.461, 4.416, 0.652, 4.002, 4.644},
	{0.679, 2.800, 5.523, 3.049, 2.968, 7.225, 6.730, 4.199, 9.614, 9.229},
	{4.263, 1.074, 7.286, 5.599, 8.291, 5.200, 9.214, 8.272, 4.398, 4.506},
	{9.496, 4.830, 3.150, 8.270, 5.079, 1.231, 5.731, 9.494, 1.883, 9.732},
	{4.138, 2.562, 2.532, 9.661, 5.611, 5.500, 6.886, 2.341, 9.699, 6.500}};
	
	double c[] = {0.806,0.517,0.100,0.908,0.965,0.669,0.524,0.902,0.531,0.876,0.462,0.491,0.463,0.714,0.352,0.869,0.813,0.811,0.828,0.964,0.789,0.360,0.369,0.992,0.332,0.817,0.632,0.883,0.608,0.326};
	int dimension=x.length;
	double sum=0.0;
	double h=0.0;
	double sp=0.0;
	for (int j=0; j<30; j++)
	{
		sp=0.0;
		for (int i=0; i<dimension; i++)
		{
			h=(x[i])-a[j][i];
			sp+=h*h;
		}
		sum-=1.0/(sp+c[j]);
	}
	
	
	return sum;
}
}

class f45 extends f_xj //Exp2 function   f(x)=0   @x(1.0,10.0)   0.0<x[i]<20.0
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
for(int i=0;i<10;i++)
{s1+=Math.pow((Math.exp(-(double)i*x[0]/10.0)-(5.0*Math.exp(-(double)i*x[1]/10.0))-Math.exp(-(double)i/10.0)+5.0*Math.exp(-(double)i)),2.0);}
return s1;
}
}

class f46 extends f_xj //Stretched V function   
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
for(int i=0;i<=n-2;i++)
{s1+=Math.pow((x[i+1]*x[i+1]+x[i]*x[i]),0.25)*Math.sin(50.0*Math.pow((x[i+1]*x[i+1]+x[i]*x[i]),0.1))+1.0;}
return s1;
}
}

class f47 extends f_xj //Trecanni function   f(x)=0   @x(0.0,0.0,....)   -5.0<x[i]<5.0
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
s1=Math.pow(x[0],4.0)+4.0*Math.pow(x[0],3.0)+4.0*Math.pow(x[0],2.0)+x[1]*x[1];
return s1;
}
}

class f48 extends f_xj //Trefethen4 function   f(x)=-3.306868   @x(-0.0244031,0.2106124)   -6.5<x[0]<6.5    -4.5<x[1]<4.5                  
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
s1=Math.exp(Math.sin(50.0*x[0]))+Math.sin(60.0*Math.exp(x[1]))+Math.sin(70.0*Math.sin(x[0]))+Math.sin(Math.sin(80.0*x[1]))-Math.sin(10.0*(x[0]+x[1]))+1.0/4.0*(x[0]*x[0]+x[1]*x[1]);
return s1;
}
}

class f49 extends f_xj //Paviani function                    
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;double fact=1.0;
for(int i=0;i<=n-1;i++)
{s1+=Math.pow(Math.log(x[i]-2.0),2.0)+Math.pow(Math.log(10.0-x[i]),2.0);}
for(int i=0;i<=n-1;i++)
{fact*=x[i];}
fact=Math.pow(fact,0.2);
return s1-fact;
}
}

class f50 extends f_xj //McCormick function      -1.5<=x[0]<=4 , -3.0<=x[1]<=4    f(x)=-1.9133  @x(-0.547,-1.54719)            
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	

double s1=Math.sin(x[0]+x[1])+Math.pow(x[0]-x[1],2.0)-1.5*x[0]+2.5*x[1]+1.0;
return s1;
}
}

class f51 extends f_xj //Leon function     f(x)=0.0  @x(1.0,1.0)           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	

double s1=100.0*(x[1]-x[0]*x[0]*x[0])*(x[1]-x[0]*x[0]*x[0])+(x[0]-1.0)*(x[0]-1.0);
return s1;
}
}

class f552 extends f_xj //Hosaki function     f(x)=-2.3458  @x(4.0,2.0) Error!!!!           
{
public double func(double x[]) 
{
int DD=x.length; 
double sum=0.0;
for(int i=0;i<DD-1;i++)
{sum+=x[i]*x[i]+2.0*x[i+1]*x[i+1]-0.3*Math.cos(3.0*Math.PI*x[i])-0.4*Math.cos(4.0*Math.PI*x[i+1])+0.7;}

return sum;
}
}


class f553 extends f_xj //Deb01           
{
public double func(double x[]) 
{
int DD=x.length; 
double sum=0.0;
for(int i=0;i<DD;i++)
{sum+=x[i]*x[i];}

return -Math.exp(-0.5*sum);
}
}


class f554 extends f_xj //zerosum           
{
public double func(double x[]) 
{
int DD=x.length; 
double sum=0.0;
for(int i=0;i<DD;i++)
{sum+=x[i];}
double ret;
if(sum==0.0)
{ret=0.0;}
else
{ret=1.0+Math.sqrt(10000*Math.abs(sum));} 

return ret;
}
}


class f53 extends f_xj //Hansen function     f(x)=-176.54  @x(-1.30,-1.42)           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double s1=0.0;double s2=0.0;
for(int i=0;i<=4;i++)
{s1+=((double)i+1.0)*Math.cos((double)i*x[0]+(double)i+1.0);}
for(int j=0;j<=4;j++)
{s2+=((double)j+1.0)*Math.cos(((double)j+2.0)*x[1]+(double)j+1.0);}
return s1*s2;
}
}

class f54 extends f_xj //Gear function     f(x)=0.0  @x(16,19,43,49)  12<=x[i]<=60           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	

double s1=Math.pow((1.0/6.931-(x[0]*x[1])/(x[2]*x[3])),2.0);
return s1;
}
}

class f55 extends f_xj //Gear function     f(x)=0.0  @x(16,19,43,49)  12<=x[i]<=60           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double s1=0.0;int n=x.length;
for(int i=0;i<=n-2;i++)
{s1+=-(x[i+1]+47.0)*Math.sin(Math.sqrt(Math.abs(x[i+1]+0.5*x[i]+47.0)))+ Math.sin(Math.sqrt(Math.abs(x[i]-(x[i+1]+47.0))))*(-x[i]);}

return s1;
}
}

class f56 extends f_xj //Chichinadze function     f(x1, x2) = f(5.90133, 0.5) = -43.3159.  -30<=x[i]<=30           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double s1=x[0]*x[0]-12.0*x[0]+11.0+10.0*Math.cos(x[0]*3.1415/2.0)+8.0*Math.sin(5.0*3.1415*x[0])-((1.0/Math.sqrt(5.0))*Math.exp(-(x[1]-0.5)*(x[1]-0.5)/2.0));

return s1;
}
}

class f57 extends f_xj //Zettl function     f(x1, x2) = f(-0.02990, 0.0) = -0.003791.  -30<=x[i]<=30           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double s1=Math.pow((x[0]*x[0]+x[1]*x[1]-2.0*x[0]),2.0)+0.25*x[0];
return s1;
}
}

class f58 extends f_xj //Plateau function     f(x1, x2) = f(0.0, 0.0,0.0,0.0,0.0) = 30.0  -5.12<=x[i]<=5.12           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
for(int i=0;i<n;i++)
{s1+=Math.abs(x[i]);}
return s1+30.0;

}
}


class f59 extends f_xj //Xin She Yang function1     f(x) = f(0.0, 0.0,0.0,0.0,0.0,....) = 0.0  -2pi<=x[i]<=2pi           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;double s2=0.0;
for(int i=0;i<n;i++)
{s1+=Math.abs(x[i]);}
for(int i=0;i<n;i++)
{s2+=-Math.sin(x[i]*x[i]);}
return s1*Math.exp(-s2);
}
}

class f60 extends f_xj //     f(x) = f(1.0,0.5,0.333,...) = 0.0  -5<=x[i]<=5           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;
for(int i=0;i<n;i++)
{s1+=Math.random()*Math.abs(x[i]-(1.0/((double)i+1.0)));}
return s1;
}
}

class f61 extends f_xj //              
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int n=x.length;double s1=0.0;double s2=0.0;double s3=0.0;
for(int i=0;i<n;i++)
{s1+=Math.sin(x[i])*Math.sin(x[i]);}
for(int i=0;i<n;i++)
{s2+=x[i]*x[i];}
double c1=(s1-Math.exp(-s2));
for(int i=0;i<n;i++)
{s3+=Math.pow(Math.sin(Math.sqrt(Math.abs(x[i]))),2.0);}
return c1*Math.exp(-s3);
}
}

class f62 extends f_xj // Yang funtion     x(pi,pi)             
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double alfa=1.0;
double beta=1.0;
int K=10;
double s1=0.0;
for(int j=0;j<K;j++)
{
  for(int i=0;i<K;i++)
  {
  s1+=Math.random()*Math.exp(-alfa*((x[0]-(double)i)*(x[0]-(double)i)+(x[1]-(double)j)*(x[1]-(double)j)));	  
  }	
	
}
double s2=-5.0*Math.exp(-beta*((x[0]-3.1415)*(x[0]-3.1415)+(x[1]-3.1415)*(x[1]-3.1415)));	

return s2-s1;

}
}


class f665 extends f_xj // Weierstrass Function             
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double a=0.5;double b=3.0;
int kmax=20;
double suminner=0.0;
double sumouter=0.0;

for(int i=0;i<DD;i++)
{   
	suminner=0.0;
	for(int k=0;k<kmax;)
	{
	suminner+=(Math.pow(a,(double)k)*Math.cos(2.0*Math.PI*Math.pow(b,(double)k)*(x[i]+0.5)));
 	}
sumouter+=suminner; 
}

double suminner1=0.0;
for(int k=0;k<kmax;k++)
{suminner1+=Math.pow(a,(double)k)*Math.cos(2.0*Math.PI*Math.pow(b,(double)k)*0.5);}
return sumouter-(double)DD*suminner1;

}

}



class f666 extends f_xj // Hgbat func            
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double sum1=0.0;
for(int i=0;i<DD;i++)
{sum1+=x[i]*x[i];}
double sum11=sum1*sum1;

double sum2=0.0;
for(int i=0;i<DD;i++)
{sum2+=x[i];}
double sum22=sum2*sum2;

double sum1122=Math.sqrt(Math.abs(sum11-sum22))+((0.5*sum1+sum2)/(double)DD)+0.5;

return sum1122;

}

}



class f667 extends f_xj // HappyCat Function            
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double sum1=0.0;
for(int i=0;i<DD;i++)
{sum1+=x[i]*x[i];}
double sum11=sum1*sum1;

double sum2=0.0;
for(int i=0;i<DD;i++)
{sum2+=x[i];}
double sum22=sum2*sum2;

double sum1122=Math.pow(Math.abs(sum1-(double)DD),0.25)+((0.5*sum1+sum2)/(double)DD)+0.5;

return sum1122;

}

}


class f668 extends f_xj // Katsuura Function  ++           
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double sumin1=0.0;
double powerin=1.0;

for(int i=0;i<DD;i++)
{  
  sumin1=0.0;	
  for(int j=1;j<=32;j++)
  {sumin1+=Math.abs(Math.pow(2.0,j)*x[i]-Math.round(Math.pow(2.0,j)*x[i]))/Math.pow(2.0,j);}

  powerin*=Math.pow((1.0+sumin1*(double)(i+1)),(10.0/Math.pow((double)DD,1.2)));
}
return (10.0/(double)DD/(double)DD)*powerin-(10.0/(double)DD/(double)DD)+200;

}

}


class f669 extends f_xj // Griewank’s plus Rosenbrock’s Function          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] f10=new double[DD];
for(int i=0;i<DD-1;i++)
{f10[i]=100*(x[i]*x[i]-x[i+1])*(x[i]*x[i]-x[i+1])+(x[i]-1)*(x[i]-1);} 
f10[DD-1]=100*(x[DD-1]*x[DD-1]-x[0])*(x[DD-1]*x[DD-1]-x[0])+(x[DD-1]-1)*(x[DD-1]-1);

double sum1=0.0;
double sum2=0.0;
for(int i=0;i<DD;i++)
{sum1+=f10[i]*f10[i]/4000.0;}
double fact=1.0;
for(int i=0;i<DD;i++)
{fact*=Math.cos(f10[i]/Math.sqrt((double)i+1));}

return sum1-fact+1;


}

}




class f670 extends f_xj // Expanded Scaffer’s F6 Function         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] f10=new double[DD];
for(int i=0;i<DD-1;i++)
{f10[i]=0.5+(Math.sin(Math.sqrt(x[i]*x[i]+x[i+1]*x[i+1]))*Math.sin(Math.sqrt(x[i]*x[i]+x[i+1]*x[i+1]))-0.5)/((1.0+0.001*(x[i]*x[i]+x[i+1]*x[i+1]))*(1.0+0.001*(x[i]*x[i]+x[i+1]*x[i+1])))             ;} 
f10[DD-1]=0.5+(Math.sin(Math.sqrt(x[DD-1]*x[DD-1]+x[0]*x[0]))*Math.sin(Math.sqrt(x[DD-1]*x[DD-1]+x[0]*x[0]))-0.5)/((1.0+0.001*(x[DD-1]*x[DD-1]+x[0]*x[0]))*(1.0+0.001*(x[DD-1]*x[DD-1]+x[0]*x[0])));

double sum1=0.0;
double sum2=0.0;
for(int i=0;i<DD;i++)
{sum1+=f10[i];}
 
return sum1;


}

}



class f671 extends f_xj // Modified Schwefel’s Function         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] f10=new double[DD];
double[] z=new double[DD]; 
double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{z[i]=x[i]+4.209687462275036*100.0;}

for(int i=0;i<DD;i++)
{
     if(Math.abs(z[i])<=500.0)
     {gz[i]=z[i]*Math.sin(Math.sqrt(Math.abs(z[i])));}
	 else if(z[i]>500.0)
	 {gz[i]=(500.0-(z[i]%500.0))*Math.sin(Math.sqrt(Math.abs(500.0-(z[i]%500.0)))) - (((z[i]-500.0)*(z[i]-500.0))/(10000.0*(double)DD)) ;} 	
	 else if(z[i]<-500.0)
	 {gz[i]=((Math.abs(z[i])%500.0)-500.0)*Math.sin(Math.sqrt((Math.abs(z[i])%500.0)-500.0))- (((z[i]+500.0)*(z[i]+500.0))/(10000.0*(double)DD)) ;                }
}

for(int i=0;i<DD;i++)
{sum1+=gz[i];}
return 418.9829*(double)DD-sum1;
}

}


class f672 extends f_xj // Expanded Two-Peak Trap        
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{y[i]=x[i]+20.0;}

for(int i=0;i<DD;i++)
{
    if(y[i]<0)
    {t[i]=-160+y[i]*y[i];}
    else if((0.0<=y[i])&&(y[i]<=15.0))
    {t[i]=160.0/15.0*(y[i]-15.0);}
    else if((15.0<=y[i])&&(y[i]<=20.0))
    {t[i]=200.0/5.0*(-y[i]+15.0);}
    else if(y[i]>20.0)
    {t[i]=-200.0+(y[i]-20.0)*(y[i]-20.0);}
    	
}

for(int i=0;i<DD;i++)
{sum1+=t[i];}

return sum1+200*DD;


}

}



class f673 extends f_xj // Expanded Five-Uneven-Peak Trap      
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

 

for(int i=0;i<DD;i++)
{
    if(x[i]<0)
    {t[i]=-200+x[i]*x[i];}
    else if((0.0<=x[i])&&(x[i]<2.5))
    {t[i]=-80.0*(2.5-x[i]);}
    else if((2.5<=x[i])&&(x[i]<5.0))
    {t[i]=-64.0*(x[i]-2.5);}
    else if((5.0<=x[i])&&(x[i]<7.5))
    {t[i]=-64.0*(7.5-x[i]);}
    else if((7.5<=x[i])&&(x[i]<12.5))
    {t[i]=-28.0*(x[i]-7.5);}
    else if((12.5<=x[i])&&(x[i]<17.5))
    {t[i]=-28.0*(17.5-x[i]);}  
    else if((17.5<=x[i])&&(x[i]<22.5))
    {t[i]=-32.0*(x[i]-17.5);}  	
    else if((22.5<=x[i])&&(x[i]<27.5))
    {t[i]=-32.0*(27.5-x[i]);} 
    else if((27.5<=x[i])&&(x[i]<=30.0))
    {t[i]=-80.0*(x[i]-27.5);} 
    else if(x[i]>30.0)
    {t[i]=-200.0+(x[i]-30.0)*(x[i]-30.0);}
}

for(int i=0;i<DD;i++)
{sum1+=t[i];}

return sum1+200*DD;


}

}



class f674 extends f_xj // Expanded Equal Minima      
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{y[i]=x[i]+0.1;} 

for(int i=0;i<DD;i++)
{
    if((y[i]<0)||(y[i]>1))
    {t[i]=y[i]*y[i];}
    else if((0.0<=y[i])&&(y[i]<=1.0))
    {t[i]=-Math.pow(Math.sin(5.0*Math.PI*y[i]),6.0);}
 
}

for(int i=0;i<DD;i++)
{sum1+=t[i];}

return sum1+DD;


}

}
class f675 extends f_xj // Expanded Decreasing Minima   
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{y[i]=x[i]+0.1;} 

for(int i=0;i<DD;i++)
{
    if((y[i]<0)||(y[i]>1))
    {t[i]=y[i]*y[i];}
    else if((0.0<=y[i])&&(y[i]<=1.0))
    {t[i]=-Math.exp(-2.0*Math.log(2.0)*((y[i]-0.1)/0.8)*((y[i]-0.1)/0.8))*Math.pow(Math.sin(5.0*Math.PI*y[i]),6.0);}
 
}

for(int i=0;i<DD;i++)
{sum1+=t[i];}

return sum1+DD;


}

}


class f676 extends f_xj // Expanded Uneven Minima   
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{y[i]=x[i]+ 0.079699392688696;} 

for(int i=0;i<DD;i++)
{
    if((y[i]<0)||(y[i]>1))
    {t[i]=y[i]*y[i];}
    else if((0.0<=y[i])&&(y[i]<=1.0))
    {t[i]=-Math.pow(Math.sin(5.0*Math.PI*(Math.pow(y[i],0.75)-0.05)),6.0);}
 
}

for(int i=0;i<DD;i++)
{sum1+=t[i];}

return sum1-DD;


}

}




class f677 extends f_xj // Expanded Himmelblau's Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{
   if((double)i%2==1)
   {y[i]=x[i]-3.0;}	
   else if((double)i%2==0)
   {y[i]=x[i]-2.0;}

}

for(int i=0;i<DD-1;i=i+2)
{sum1+= ((y[i]*y[i]+y[i+1]-11.0)*(y[i]*y[i]+y[i+1]-11.0))+((y[i]+y[i+1]*y[i+1]-7.0)*(y[i]+y[i+1]*y[i+1]-7.0));                  } 

 

return sum1;


}

}



class f678 extends f_xj // Modified Vincent Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] y=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{y[i]=x[i]+4.1112;}

for(int i=0;i<DD;i++)
{
   if((0.25<=y[i])&&(y[i]<=10.0))
   {t[i]=Math.sin(10.0*Math.log(y[i]));}	
   else if (y[i]<0.25)
   {t[i]=(0.25-y[i])*(0.25-y[i])+Math.sin(10.0*Math.log(2.5));}
   else if (y[i]>10.0)
   {t[i]=(-10.0+y[i])*(-10.0+y[i])+Math.sin(10.0*Math.log(10.0));}    	
	
	
}

for(int i=0;i<DD;i++)
{sum1+=(t[i]+0.1);}
sum1=sum1/(double)DD; 
 

return sum1;


}

}



 



class f679 extends f_xj // Shifted Sphere Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] z=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{z[i]=x[i]-1.0;}

for(int i=0;i<DD;i++)
{sum1+=z[i]*z[i];}

sum1=sum1-450;
 

return sum1;


}

}




class f680 extends f_xj // Shifted Rastrigin’s Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] z=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{z[i]=x[i]-1.0;}

for(int i=0;i<DD;i++)
{sum1+=z[i]*z[i]-10.0*Math.cos(2.0*Math.PI*z[i])+10.0;}

sum1=sum1-330;
 

return sum1;


}

}


class f681 extends f_xj // Shifted Griewank’s Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] z=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;

for(int i=0;i<DD;i++)
{z[i]=x[i]-1.0;}

for(int i=0;i<DD;i++)
{sum1+=z[i]*z[i]/4000.0;}

double fakt=1.0;

for(int i=0;i<DD;i++)
{fakt*=Math.cos(z[i]/(Math.sqrt(i+1)));}

double ret=sum1-fakt+1-180;
//sum1=sum1-330;
 

return ret;


}

}


class f682 extends f_xj // Shifted Ackley’s Function 
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
int DD=x.length;
double[] z=new double[DD];
double[] t=new double[DD]; 
//double[] gz=new double[DD];

double sum1=0.0;
double sum2=0.0;
for(int i=0;i<DD;i++)
{z[i]=x[i]-1.0;}

for(int i=0;i<DD;i++)
{sum1+=z[i]*z[i];}
double part1=-20.0*Math.exp(-0.2*Math.sqrt(sum1/(double)DD));

for(int i=0;i<DD;i++)
{sum2+=Math.cos(2.0*Math.PI*z[i]);}
sum2=sum2/(double)DD;
double part2=Math.exp(sum2);
 
double ret=part1-part2+20.0+Math.exp(1)-140.0;

 
//sum1=sum1-330;
 

return ret;


}

}


//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////// CONSTRAINED OPTIMIZATION ////////////////////////////
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////





// Minimize f(x)=(x[0]-6.0)*(x[0]-6.0)+(x[1]-7.0)*(x[1]-7.0)
//constrains
// g1(x)=-3*x[0]-2*x[1]+6.0<=0.0
// g2(x)=-x[0]-x[1]-3.0<=0.0
// g3(x)=x[0]+x[1]-7.0<=0.0
// g4(x)=0.66*x[0]-x[1]-1.333<=0.0
class f63 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=8.0;
return (x[0]-6.0)*(x[0]-6.0)+(x[1]-7.0)*(x[1]-7.0)+mu*(G1(x)+G2(x)+G3(x)+G4(x));
}

double G1(double x[])
{
return Math.pow(Math.max(0,-3*x[0]-2*x[1]+6.0),2.0);	
}
double G2(double x[])
{
return Math.pow(Math.max(0,-x[0]-x[1]-3.0),2.0);	
}
double G3(double x[])
{
return Math.pow(Math.max(0,x[0]+x[1]-7.0),2.0);	
}
double G4(double x[])
{
return Math.pow(Math.max(0,0.66*x[0]-x[1]-1.333),2.0);	
}
}
// Water cycle makalesinden alýndý

// Minimize ((x[0]-10.0)*(x[0]-10.0))+(5.0*(x[1]-12.0)*(x[1]-12.0))+(x[2]*x[2]*x[2]*x[2])+(3.0*(x[3]-11.0)*(x[3]-11.0))+(10.0*(x[4]*x[4]*x[4]*x[4]*x[4]*x[4]))+(7.0*x[5]*x[5])+(x[6]*x[6]*x[6]*x[6])-(4.0*x[5]*x[6])-10*x[5]-8.0*x[6];
// constrains
// g1(x)=127.0-2.0*x[0]*x[0]-3.0*x[1]*x[1]*x[1]*x[1]-x[2]-4.0*x[3]*x[3]-5.0*x[4]>=0.0
// g2(x)=282.0-7.0*x[0]-3.0*x[1]-10.0*x[2]*x[2]-x[3]+x[4]>=0.0
// g3(x)=196.0-23.0*x[0]-x[1]*x[1]-6.0*x[5]*x[5]+8.0*x[6]>=0.0
// g4(x)=-4.0*x[0]*x[0]-x[1]*x[1]+3.0*x[0]*x[1]-2.0*x[2]*x[2]-5.0*x[5]+11.0*x[6]>=0.0

class f64 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.25;
return ((x[0]-10.0)*(x[0]-10.0))+(5.0*(x[1]-12.0)*(x[1]-12.0))+(x[2]*x[2]*x[2]*x[2])+(3.0*(x[3]-11.0)*(x[3]-11.0))+(10.0*(x[4]*x[4]*x[4]*x[4]*x[4]*x[4]))+(7.0*x[5]*x[5])+(x[6]*x[6]*x[6]*x[6])-(4.0*x[5]*x[6])-10*x[5]-8.0*x[6]+mu*(G1(x)+G2(x)+G3(x)+G4(x));
}

double G1(double x[])
{
return Math.pow(Math.min(0.0,127.0-2.0*x[0]*x[0]-3.0*x[1]*x[1]*x[1]*x[1]-x[2]-4.0*x[3]*x[3]-5.0*x[4]),2.0);	
}
double G2(double x[])
{
return Math.pow(Math.min(0.0,282.0-7.0*x[0]-3.0*x[1]-10.0*x[2]*x[2]-x[3]+x[4]),2.0);	
}
double G3(double x[])
{
return Math.pow(Math.min(0.0,196.0-23.0*x[0]-x[1]*x[1]-6.0*x[5]*x[5]+8.0*x[6]),2.0);	
}
double G4(double x[])
{
return Math.pow(Math.min(0.0,-4.0*x[0]*x[0]-x[1]*x[1]+3.0*x[0]*x[1]-2.0*x[2]*x[2]-5.0*x[5]+11.0*x[6]),2.0);	
}
}


// minimize 5.3578547*x[2]*x[2]*x[2]+0.8356891*x[0]*x[4]+37.203239*x[0]+40729.141
// constraints
// g1(x)=85.334407+0.0056858*x[1]*x[4]+0.0006262*x[0]*x[3]-0.0022053*x[2]*x[4]-92.0<=0.0
// g2(x)=-85.334407-0.0056858*x[1]*x[4]-0.0006262*x[0]*x[3]-0.0022053*x[2]*x[4]<=0.0
// g3(x)=80.51249+0.0071317*x[1]*x[4]+0.0029955*x[0]*x[1]+0.0021813*x[2]*x[2]-110.0<=0.0
// g4(x)=-80.51249-0.0071317*x[1]*x[4]-0.0029955*x[0]*x[1]-0.0021813*x[2]*x[2]+90.0<=0.0
// g5(x)=9.300961+0.0047026*x[2]*x[4]+0.0012547*x[0]*x[2]+0.0019085*x[2]*x[3]-25.0<=0.0
// g6(x)=-9.300961-0.0047026*x[2]*x[4]-0.0012547*x[0]*x[2]-0.0019085*x[2]*x[3]+20.0<=0.0
// 78<x[0]<102, 33<x[1]<45, 27<x[i]<45 i=2,3,4
class f65 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1953;
return (5.3578547*x[2]*x[2])+(0.8356891*x[0]*x[4])+(37.203239*x[0])-(40729.141)+(mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)+G6(x)));
}

double G1(double x[])
{
return Math.pow(Math.max(0.0,85.334407+0.0056858*x[1]*x[4]+0.0006262*x[0]*x[3]-0.0022053*x[2]*x[4]-92.0),2.0);	
}
double G2(double x[])
{
return Math.pow(Math.max(0.0,-85.334407-0.0056858*x[1]*x[4]-0.0006262*x[0]*x[3]+0.0022053*x[2]*x[4]),2.0);	
}
double G3(double x[])
{
return Math.pow(Math.max(0.0,80.51249+0.0071317*x[1]*x[3]+0.0029955*x[0]*x[1]+0.0021813*x[2]*x[2]-110.0),2.0);	
}
double G4(double x[])
{
return Math.pow(Math.max(0.0,-80.51249-0.0071317*x[1]*x[3]-0.0029955*x[0]*x[1]-0.0021813*x[2]*x[2]+90.0),2.0);	
}
double G5(double x[])
{
return Math.pow(Math.max(0.0,9.300961+0.0047026*x[2]*x[4]+0.0012547*x[0]*x[2]+0.0019085*x[2]*x[3]-25.0),2.0);	
}
double G6(double x[])
{
return Math.pow(Math.max(0.0,-9.300961-0.0047026*x[2]*x[4]-0.0012547*x[0]*x[2]-0.0019085*x[2]*x[3]+20.0),2.0);	
}
}

class f66 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1.1;
double s1=1.0;
int n=x.length;
for(int i=0;i<n;i++)
{s1*=x[i];}
s1=-Math.pow(Math.sqrt((double)n),(double)n)*s1;
return s1+(mu*G1(x));
}

double G1(double x[])
{
double c1=0.0;
int n=x.length;
for(int i=0;i<n;i++)
{c1+=(x[i]*x[i]);}
c1=c1-1;

return Math.abs(c1)-1e-4;
}
}

class f67 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1.2;
double s1=((3.0*x[0])+(1e-6*x[0]*x[0]*x[0])+(2.0*x[1])+((2e-6/3.0)*x[1]*x[1]*x[1]))+mu*(G1(x)+G2(x)+H1(x)+H2(x)+H3(x));
return s1;
}

double G1(double x[])
{
return  Math.pow(Math.max(0.0,x[2]-x[3]-0.55),2.0);	
}

double G2(double x[])
{
return Math.pow(Math.max(0.0,x[3]-x[2]-0.55),2.0);		
}

double H1(double x[])
{
return Math.abs(1000.0*(Math.sin(-x[2]-0.25)+Math.sin(-x[3]-0.25))+894.8-x[0])-1e-4;	
}

double H2(double x[])
{
return Math.abs(1000.0*(Math.sin(x[2]-0.25)+Math.sin(x[2]-x[3]-0.25))+894.8-x[1])-1e-4;	
}

double H3(double x[])
{
return Math.abs(1000.0*(Math.sin(x[3]-0.25)+Math.sin(x[3]-x[2]-0.25))+1294.8)-1e-4;	
}
}

class f68 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.007;
return ((x[0]-10.0)*(x[0]-10.0)*(x[0]-10.0))+((x[1]-20.0)*(x[1]-20.0)*(x[1]-20.0))+(mu*(G1(x)+G2(x)));
}

double G1(double x[])
{
return Math.pow(Math.max(0.0,(x[0]-5.0)*(x[0]-5.0)+(x[1]-5.0)*(x[1]-5.0)+100.0),2.0);
}
double G2(double x[])
{
return Math.pow(Math.max(0.0,(x[0]-5.0)*(x[0]-5.0)+(x[1]-5.0)*(x[1]-5.0)-82.81),2.0);
}
}

class f69 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1.0;
double s1=x[0]*x[0]+(x[1]-1.0)*(x[1]-1.0)+mu*(H1(x));
return s1;
}

double H1(double x[])
{
return Math.abs(x[1]-x[0]*x[0])-1e-4;
}
}

class f70 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.012;
double s1=Math.exp(x[0]*x[1]*x[2]*x[3]*x[4])+mu*(H1(x)+H2(x)+H3(x));
return s1;
}

double H1(double x[])
{
  int n=x.length;
  double d=0.0;
  for(int i=0;i<n;i++)
  {d+=x[i]*x[i];}
  return Math.abs(d-10.0)-1e-4; 	
	
}

double H2(double x[])
{
return  Math.abs(x[1]*x[2]-5.0*x[3]*x[4])-1e-4; 	
}

double H3(double x[])
{
return Math.abs(x[0]*x[0]*x[0]+x[1]*x[1]*x[1]+1.0)-1e-4;	
}

}

class f71 extends f_xj   //minr blsdt 1       
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=180.0;
double s1=(x[0]-2.0)*(x[0]-2.0)+(x[1]-1.0)*(x[1]-1.0)+(mu*(H1(x)+H2(x)));
return s1;
}

double H1(double x[])
{return Math.abs(x[0]-2.0*x[1]+1.0)-1e-4;}

double H2(double x[])
{return  Math.pow(Math.min(0.0,(-x[0]*x[0]/4.0)-x[1]*x[1]+1.0),2.0);}
}

class f72 extends f_xj   //minr blsdt 3       
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=10000.0;
double s1=(x[0]*x[0]+x[1]-11.0)*(x[0]*x[0]+x[1]-11.0)+(x[0]+x[1]*x[1]-7.0)*(x[0]+x[1]*x[1]-7.0)+(mu*(H1(x)+H2(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.min(0.0,4.84-((x[0]-0.05)*(x[0]-0.05))-((x[1]-2.5)*(x[1]-2.5))),2.0);}

double H2(double x[])
{return  Math.pow(Math.min(0.0,x[0]*x[0]+(x[1]-2.5)*(x[1]-2.5)-4.84),2.0);}
}


// Three bar truss design problem
class f73 extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=10000000.0;
double s1=((2.0*Math.sqrt(2.0)*x[0]+x[1])*100)+mu*(H1(x)+H2(x)+H3(x));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0, (((Math.sqrt(2.0)*x[0]+x[1])/(Math.sqrt(2)*x[0]*x[0]+2.0*x[0]*x[1]))*2.0)-2.0  ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,    2.0*(x[1]/(Math.sqrt(2)*x[0]*x[0]+2.0*x[0]))-2.0),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,2.0*(1.0/(Math.sqrt(2.0)*x[1]+x[0]))-2.0),2.0);}
}

// Presure vessel design problem !!!error
class f74 extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1.0;
double s1=(0.6224*x[0]*x[2]*x[3])+(1.7781*x[1]*x[2]*x[2])+(3.1661*x[0]*x[0]*x[3])+(19.84*x[0]*x[0]*x[2])+(mu*(H1(x)+H2(x)+H3(x)+H4(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.min(0.0, -x[0]+0.0193*x[2]),2.0);}

double H2(double x[])
{return  Math.pow(Math.min(0.0, -x[1]+0.00954*x[2]),2.0);}

double H3(double x[])
{return  Math.pow(Math.min(0.0,-3.1415*x[2]*x[2]*x[3]-(1.333)*3.1415*x[2]*x[2]*x[2]+1296000),2.0);}

double H4(double x[])
{return  Math.pow(Math.min(0.0,x[3]-240.0),2.0);}
}


//Tension/compression spring design problem
class f75 extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.3;
double s1=((x[2]+2.0)*x[1]*x[0]*x[0])+(mu*(H1(x)+H2(x)+H3(x)+H4(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         1.0-((x[1]*x[1]*x[1]*x[2])/(71785.0*x[0]*x[0]*x[0]*x[0]))                  ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,        (((4.0*x[1]*x[1])-(x[0]*x[1]))/(12566.0*(x[1]*x[0]*x[0]*x[0]-x[0]*x[0]*x[0]*x[0])))+(1.0/(5108.0*x[0]*x[0]))-1.0              ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,        1.0-((140.45*x[0])/(x[1]*x[1]*x[2]))        ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,     ((x[0]+x[1])/1.5)-1.0        ),2.0);}
}


// Welded beam design

class f76 extends f_xj         
{
	
double P=6000.0;
double L=14.0;
double E=30000000.0;	
double G=12000000.0;
double tomax=13600.0;
double sigmax=30000.0;
double deltamax=0.25;

double sigma(double x[])
{return (6.0*P*L)/(x[3]*x[2]*x[2]);}

double delta(double x[])
{return (4.0*P*L*L*L)/(E*x[2]*x[2]*x[2]*x[3]);}

double Pc(double x[])
{return (4.013*E/(6.0*L*L))*x[2]*x[3]*x[3]*x[3]*(1-0.25*x[2]*Math.sqrt(E/G)/L);}
	
double J(double x[])
{return   2.0/Math.sqrt(2)*x[0]*x[1]*(x[1]*x[1]/12.0+0.25*(x[0]+x[2])*(x[0]+x[2]));                    }
	
double R(double x[])
{return	Math.sqrt(0.25*(x[1]*x[1]+(x[0]+x[2])*(x[0]+x[2])));}

double M(double x[])
{return P*(L+x[1]/2.0);}

double to1(double x[])
{return P/(Math.sqrt(2)*x[0]*x[1]);}

double to2(double x[])
{return M(x)*R(x)/J(x);}

double toend(double x[])
{return Math.sqrt(to1(x)*to1(x)+(2.0*to1(x)*to2(x)*x[1]/(2.0*R(x)))+to2(x)*to2(x));}
	
	
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.0000000015;
double s1=(1.10471*x[0]*x[0]*x[1])+(0.04811*x[2]*x[3]*(14.0+x[1]))+(mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         toend(x)-tomax                 ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,         sigma(x)-sigmax           ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,        x[0]-x[3]        ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,       0.10471*x[0]*x[0]+0.04811*x[2]*x[3]*(14.0+x[1])-5.0         ),2.0);}

double H5(double x[])
{return  Math.pow(Math.max(0.0,      0.125-x[0]         ),2.0);}

double H6(double x[])	
{return  Math.pow(Math.max(0.0,       delta(x)-deltamax         ),2.0);}

double H7(double x[])
{return  Math.pow(Math.max(0.0,       P-Pc(x)         ),2.0);}

}


// speed reducer design problem
class f77 extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1820000000000000.7;
double s1=0.7854*x[0]*x[1]*x[1]*(3.3333*x[2]*x[2]+14.9334*x[2]-43.0934)-(1.508*x[0]*(x[5]*x[5]+x[6]*x[6]))+(7.4777*(x[5]*x[5]*x[5]+x[6]*x[6]*x[6]))+(0.7854*(x[3]*x[5]*x[5]+x[4]*x[6]*x[6]))+(mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)+H8(x)+H9(x)+H10(x)+H11(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         (27.0/(x[0]*x[1]*x[1]*x[2]))-1.0                 ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,       (397.5/(x[0]*x[1]*x[1]*x[2]*x[2]))-1.0             ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,      ((1.93*x[3]*x[3]*x[3])/(x[1]*x[5]*x[5]*x[5]*x[5]*x[2]))-1.0          ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,     ((1.93*x[4]*x[4]*x[4])/(x[1]*x[6]*x[6]*x[6]*x[6]*x[2]))-1.0        ),2.0);}

double H5(double x[])
{return  Math.pow(Math.max(0.0,     (Math.sqrt((745.0*(x[3]/(x[1]*x[2])))*(745.0*(x[3]/(x[1]*x[2])))+16.9e6)/(110.0*x[5]*x[5]*x[5]))-1.0                                    ),2.0);}

double H6(double x[])
{return  Math.pow(Math.max(0.0,    (Math.sqrt((745.0*(x[4]/(x[1]*x[2])))*(745.0*(x[3]/(x[1]*x[2])))+157.5e6)/(85.0*x[6]*x[6]*x[6]))-1.0     ),2.0);}

double H7(double x[])
{return  Math.pow(Math.max(0.0,     (x[1]*x[2]/40.0)-1.0        ),2.0);}

double H8(double x[])
{return  Math.pow(Math.max(0.0,     (5.0*x[1]/x[0])-1.0        ),2.0);}

double H9(double x[])
{return  Math.pow(Math.max(0.0,     (x[0]/(12.0*x[1]))-1.0      ),2.0);}

double H10(double x[])
{return  Math.pow(Math.max(0.0,     ((1.5*x[5]+1.9)/x[3])-1.0        ),2.0);}

double H11(double x[])
{return  Math.pow(Math.max(0.0,     ((1.1*x[6]+1.9)/x[4])-1.0       ),2.0);}


}



class par_est2 extends f_xj
{
	
public double func(double x[])
{
double[] I1={4.84992,10.7714,16.0843,21.1287,26.5745,32.2935,37.7378,42.7127,48.5654,54.0119,59.3275,65.3104,70.2269,75.6784};
double[] V1={0.921044,0.875721,0.848175,0.819634,0.798022,0.756648,0.741955,0.723297,0.684891,0.660314,0.620907,0.600297,0.544075,0.497752};
double[] V_dummy1=new double[14];
double N=1.0;
//double Jmax=0.86;//I/cm2
double RHa=1.0;
double RHb=1.0;
double A=50.6;//cm2
double l=178.0e-4;//cm
//initialize
double Rm=0.0;
double T=343.15;//K
double PO2=1.0;//atm
double PH2=1.0;
double V_Ernst=0.0;
double CO2=0.0;
double V_act=0.0;
double ro_m=0.0;
double R_m=0.0;
double V_ohm=0.0;
double V_con=0.0;

for(int i=0;i<14;i++)
{
    
    
			
	   
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(  (I1[i]+(x[10]/x[9]))   )));
        ro_m=181.6*(1.0+(0.03*(   (I1[i]+(x[10]/x[9]))    /x[9]  ))+(0.062*(T/303.0)*(T/303.0)*Math.pow((     (I1[i]+(x[10]/x[9]))   /   x[9]),2.5)))/((x[4]-0.634-3.0*(   (I1[i]+(x[10]/x[9]))  /    x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=(I1[i]+(x[10]/x[9]))*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((( (I1[i]+(x[10]/x[9]))  /  x[9]))/x[8]));
	    V_dummy1[i]=N*(V_Ernst-V_act-V_ohm-V_con);
    
	
	
	
	
}

double val1=0.0;	
for(int i=0;i<14;i++)
{val1+=(V1[i]-V_dummy1[i])*(V1[i]-V_dummy1[i]);}

return val1;	
	
}	


double psat(double T)
{
return	Math.pow(10.0,((2.95e-2*(T-273.15))-((9.18e-5)*(T-273.15)*(T-273.15))+((1.44e-7)*(T-273.15)*(T-273.15)*(T-273.15))-2.18));	
}	
	
double bartoatm(double barr)
{return barr*0.986923267;}	
	
	
	
}


class par_est extends f_xj
{
	
public double func(double x[])
{
double[] I1={0.227213,1.3085,2.6534,3.99747,5.36203,6.70621,8.05038,10.7587,13.4467,16.1351,17.4995,18.8436,20.208,21.5522,22.9172};
double[] V1={23.5485,21.4567,20.3221,19.935,19.4284,18.9516,18.4748,17.8501,17.2553,16.212,15.8548,15.4976,15.1405,14.6338,13.6786};
double[] I2={0.224307,1.30506,2.6509,3.99674,5.36297,6.70881,8.05465,10.7463,13.4584,16.1705,17.5163,18.8825,20.2284,21.5742,22.9201};
double[] V2={21.5522,19.7015,18.7463,18.0,17.5522,17.1642,16.6567,15.9104,15.1642,14.4478,14.0299,13.5522,12.6567,10.8955,8.89552};
double[] I3={0.285837,1.32988,2.66286,4.05778,5.39127,6.76593,8.079,10.7874,13.4959,16.1836,17.5584,18.8716,20.2667,21.5792,22.9325};
double[] V3={23.235,21.0279,20.0851,19.4364,18.9054,18.4626,18.0199,17.2814,16.543,15.6869,15.3029,14.9484,14.4762,13.5923,12.5024};
double[] I4={0.248484,1.29844,2.65425,4.03034,5.36496,6.74076,8.07542,10.7858,13.4961,16.1653,17.5409,18.8553,20.2521,21.5878,22.9242};
double[] V4={22.6822,20.1581,19.2495,18.5463,18.1662,17.698,17.2885,16.4695,15.7092,14.9782,14.6862,14.0711,13.2212,12.0188,10.1705};


double[][] V_dummy=new double[4][15];
double[][] V_real=new double[4][15];
double[][] Ireal=new double[4][15];

for(int j=0;j<15;j++)
{V_real[0][j]=V1[j];}	
for(int j=0;j<15;j++)
{V_real[1][j]=V2[j];}	
for(int j=0;j<15;j++)
{V_real[2][j]=V3[j];}	
for(int j=0;j<15;j++)
{V_real[3][j]=V4[j];}

for(int j=0;j<15;j++)
{Ireal[0][j]=I1[j];}	
for(int j=0;j<15;j++)
{Ireal[1][j]=I2[j];}	
for(int j=0;j<15;j++)
{Ireal[2][j]=I3[j];}	
for(int j=0;j<15;j++)
{Ireal[3][j]=I4[j];}

double N=24.0;
double Jmax=0.86;//I/cm2
double RHa=1.0;
double RHb=1.0;
double A=27.0;//cm2
double l=127.0e-4;//cm


//initialize





double Rm=0.0;


double Pa_atm=0.0;
double Pb_atm=0.0;
double T=0.0;//K
double Pa=0.0;//bar
double Pb=0.0;//bar
double PO2=0.0;//atm
double PH2=0.0;
double V_Ernst=0.0;
double CO2=0.0;
double V_act=0.0;
double ro_m=0.0;
double R_m=0.0;
double V_ohm=0.0;
double V_con=0.0;

for(int i=0;i<4;i++)
{
    if(i==0)
    {
	 T=353.15;    
	 Pa=3.0;
	 Pb=5.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/A))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/A),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/A))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*l/A;
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/A)/Jmax));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	if(i==1)
    {
	 T=343.15;    
	 Pa=1.0;
	 Pb=1.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/A))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/A),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/A))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*l/A;
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/A)/Jmax));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}	
	if(i==2)
    {
	 T=343.15;    
	 Pa=2.5;
	 Pb=3.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/A))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/A),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/A))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*l/A;
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/A)/Jmax));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	if(i==3)
    {
	 T=343.15;    
	 Pa=1.5;
	 Pb=1.5; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/A)/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/A))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/A),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/A))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*l/A;
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/A)/Jmax));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	
}

double val=0.0;	
for(int i=0;i<4;i++)
{
  for(int j=0;j<15;j++)
  {
  val+=(V_real[i][j]-V_dummy[i][j])*(V_real[i][j]-V_dummy[i][j]);   	  
  }	
}

return val;	
	
}	


double psat(double T)
{
return	Math.pow(10.0,((2.95e-2*(T-273.15))-((9.18e-5)*(T-273.15)*(T-273.15))+((1.44e-7)*(T-273.15)*(T-273.15)*(T-273.15))-2.18));	
}	
	
double bartoatm(double barr)
{return barr*0.986923267;}	
}

class par_est4 extends f_xj
{
	
public double func(double x[])
{
double[] I1={0.01,4.99101,10.009,15.0,19.964,25.036,30.027};
double[] V11={30.1304,24.8478,22.1087,20.25,19.2717,18.6848,17.1196};
double[] V_dummy1=new double[V11.length];
double N=32.0;
//double Jmax=0.86;//I/cm2
double RHa=1.0;
double RHb=1.0;
double A=50.6;//cm2
double l=178.0e-4;//cm
//initialize
double Rm=0.0;
double T=333.0;//K
double PO2=0.2095;//atm
double PH2=1.000;
double V_Ernst=0.0;
double CO2=0.0;
double V_act=0.0;
double ro_m=0.0;
double R_m=0.0;
double V_ohm=0.0;
double V_con=0.0;

for(int i=0;i<V11.length;i++)
{
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(  (I1[i]+(x[10]/x[9]))   )));
        ro_m=181.6*(1.0+(0.03*(   (I1[i]+(x[10]/x[9]))    /x[9]  ))+(0.062*(T/303.0)*(T/303.0)*Math.pow((     (I1[i]+(x[10]/x[9]))   /   x[9]),2.5)))/((x[4]-0.634-3.0*(   (I1[i]+(x[10]/x[9]))  /    x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=(I1[i]+(x[10]/x[9]))*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((( (I1[i]+(x[10]/x[9]))  /  x[9]))/x[8]));
	    V_dummy1[i]=N*(V_Ernst-V_act-V_ohm-V_con);
}

double val1=0.0;	
for(int i=0;i<V11.length;i++)
{val1+=(V11[i]-V_dummy1[i])*(V11[i]-V_dummy1[i]);}

return val1;	
	
}	


double psat(double T)
{return	Math.pow(10.0,((2.95e-2*(T-273.15))-((9.18e-5)*(T-273.15)*(T-273.15))+((1.44e-7)*(T-273.15)*(T-273.15)*(T-273.15))-2.18));}	
	
double bartoatm(double barr)
{return barr*0.986923267;}	
	
}


class par_est3 extends f_xj
{
	
public double func(double x[])
{
double[] I1={0.227213,1.3085,2.6534,3.99747,5.36203,6.70621,8.05038,10.7587,13.4467,16.1351,17.4995,18.8436,20.208,21.5522,22.9172};
double[] V1={23.5485,21.4567,20.3221,19.935,19.4284,18.9516,18.4748,17.8501,17.2553,16.212,15.8548,15.4976,15.1405,14.6338,13.6786};
double[] I2={0.224307,1.30506,2.6509,3.99674,5.36297,6.70881,8.05465,10.7463,13.4584,16.1705,17.5163,18.8825,20.2284,21.5742,22.9201};
double[] V2={21.5522,19.7015,18.7463,18.0,17.5522,17.1642,16.6567,15.9104,15.1642,14.4478,14.0299,13.5522,12.6567,10.8955,8.89552};
double[] I3={0.285837,1.32988,2.66286,4.05778,5.39127,6.76593,8.079,10.7874,13.4959,16.1836,17.5584,18.8716,20.2667,21.5792,22.9325};
double[] V3={23.235,21.0279,20.0851,19.4364,18.9054,18.4626,18.0199,17.2814,16.543,15.6869,15.3029,14.9484,14.4762,13.5923,12.5024};
double[] I4={0.248484,1.29844,2.65425,4.03034,5.36496,6.74076,8.07542,10.7858,13.4961,16.1653,17.5409,18.8553,20.2521,21.5878,22.9242};
double[] V4={22.6822,20.1581,19.2495,18.5463,18.1662,17.698,17.2885,16.4695,15.7092,14.9782,14.6862,14.0711,13.2212,12.0188,10.1705};


double[][] V_dummy=new double[4][15];
double[][] V_real=new double[4][15];
double[][] Ireal=new double[4][15];

for(int j=0;j<15;j++)
{V_real[0][j]=V1[j];}	
for(int j=0;j<15;j++)
{V_real[1][j]=V2[j];}	
for(int j=0;j<15;j++)
{V_real[2][j]=V3[j];}	
for(int j=0;j<15;j++)
{V_real[3][j]=V4[j];}

for(int j=0;j<15;j++)
{Ireal[0][j]=I1[j];}	
for(int j=0;j<15;j++)
{Ireal[1][j]=I2[j];}	
for(int j=0;j<15;j++)
{Ireal[2][j]=I3[j];}	
for(int j=0;j<15;j++)
{Ireal[3][j]=I4[j];}

double N=24.0;
//double Jmax=0.86;//I/cm2
double RHa=1.0;
double RHb=1.0;
//double A=27.0;//cm2
//double l=127.0e-4;//cm


//initialize





double Rm=0.0;


double Pa_atm=0.0;
double Pb_atm=0.0;
double T=0.0;//K
double Pa=0.0;//bar
double Pb=0.0;//bar
double PO2=0.0;//atm
double PH2=0.0;
double V_Ernst=0.0;
double CO2=0.0;
double V_act=0.0;
double ro_m=0.0;
double R_m=0.0;
double V_ohm=0.0;
double V_con=0.0;

for(int i=0;i<4;i++)
{
    if(i==0)
    {
	 T=353.15;    
	 Pa=3.0;
	 Pb=5.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/x[9]))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/x[9]),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/x[9])/x[8]));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	if(i==1)
    {
	 T=343.15;    
	 Pa=1.0;
	 Pb=1.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/x[9]))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/x[9]),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/x[9])/x[8]));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}	
	if(i==2)
    {
	 T=343.15;    
	 Pa=2.5;
	 Pb=3.0; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/x[9]))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/x[9]),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/x[9])/x[8]));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	if(i==3)
    {
	 T=343.15;    
	 Pa=1.5;
	 Pb=1.5; 
	 Pa_atm=bartoatm(Pa);
	 Pb_atm=bartoatm(Pb);  
	 for(int j=0;j<15;j++)
     { 		
	    PO2=RHb*psat(T)*((1.0/(Math.exp((4.192*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHb*psat(T)/Pb_atm)))-1.0);
		PH2=0.5*RHa*psat(T)*((1.0/(Math.exp((1.635*Ireal[i][j]/x[9])/(Math.pow(T,1.334)))*(RHa*psat(T)/Pa_atm)))-1.0);
        V_Ernst=1.229-(0.85e-3*(T-298.15))+(4.3085e-5*T*(Math.log(PH2)+0.5*Math.log(PO2)));
        CO2=PO2/(5.086e6*Math.exp(-498.0/T));
        V_act=-(x[0]+(x[1]*T)+(x[2]*T*Math.log(CO2))+(x[3]*T*Math.log(Ireal[i][j])));
        ro_m=181.6*(1.0+(0.03*(Ireal[i][j]/x[9]))+(0.062*(T/303.0)*(T/303.0)*Math.pow((Ireal[i][j]/x[9]),2.5)))/((x[4]-0.634-3.0*(Ireal[i][j]/x[9]))*Math.exp(4.18*((T-303.0)/T)));
	    R_m=ro_m*x[7]/x[9];
	    V_ohm=Ireal[i][j]*(R_m+x[5]);
	    V_con=-x[6]*Math.log(1.0-((Ireal[i][j]/x[9])/x[8]));
	    V_dummy[i][j]=N*(V_Ernst-V_act-V_ohm-V_con);
     }	    
	}
	
}

double val=0.0;	
for(int i=0;i<4;i++)
{
  for(int j=0;j<15;j++)
  {
  val+=(V_real[i][j]-V_dummy[i][j])*(V_real[i][j]-V_dummy[i][j]);   	  
  }	
}

return val;	
	
}	


double psat(double T)
{
return	Math.pow(10.0,((2.95e-2*(T-273.15))-((9.18e-5)*(T-273.15)*(T-273.15))+((1.44e-7)*(T-273.15)*(T-273.15)*(T-273.15))-2.18));	
}	
	
double bartoatm(double barr)
{return barr*0.986923267;}	
}


class solar2 extends f_xj
{
double func(double x[])
{
// double  diode	
double[] Vt={-0.2057,-0.1291,-0.0588,0.0057,0.0646,0.1185,0.1678,0.2132,0.2545,0.2924,0.3269,0.3585,0.3873,0.4137,0.4373,0.459,0.4784,0.496,0.5119,0.5265,0.5398,0.5521,0.5633,0.5736,0.5833,0.59};
double[] It={0.764,0.762,0.7605,0.7605,0.76,0.759,0.757,0.757,0.7555,0.754,0.7505,0.7465,0.7385,0.728,0.7065,0.6755,0.632,0.573,0.499,0.413,0.3165,0.212,0.1035,-0.01,-0.123,-0.21};
//Rs    x[0]
//Rsh   x[1]
//IPh   x[2]	
//Isd1  x[3]	
//n1    x[4]
//Isd2  x[5]
//n2    x[6]
double q=1.602176565e-19;// electronic charge
double kb=1.3806505e-23;//Boltzmann
double T=273.15+33.0;

int m=Vt.length;
double sum=0.0;


for(int i=0;i<m;i++)
{sum+=(It[i]-x[2]+(x[3]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[4]*kb*T))-1.0))+(x[5]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[6]*kb*T))-1.0))+((Vt[i]+x[0]*It[i])/(x[1])))*(It[i]-x[2]+(x[3]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[4]*kb*T))-1.0))+(x[5]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[6]*kb*T))-1.0))+((Vt[i]+x[0]*It[i])/(x[1]))) ;}
	
return Math.sqrt(sum/(double)m);	
}	
}

class solar1 extends f_xj// Himmelblau f(x)=0.0  @x=(3.0,2.0),(-2.8051,3.1313),(-3.7793,-3.2831),(3.5844,-1.8481)   -6.0<=x[i]<=6.0 i=0,1
{
double func(double x[])
{
// single diode	
double[] Vt={-0.2057,-0.1291,-0.0588,0.0057,0.0646,0.1185,0.1678,0.2132,0.2545,0.2924,0.3269,0.3585,0.3873,0.4137,0.4373,0.459,0.4784,0.496,0.5119,0.5265,0.5398,0.5521,0.5633,0.5736,0.5833,0.59};
double[] It={0.764,0.762,0.7605,0.7605,0.76,0.759,0.757,0.757,0.7555,0.754,0.7505,0.7465,0.7385,0.728,0.7065,0.6755,0.632,0.573,0.499,0.413,0.3165,0.212,0.1035,-0.01,-0.123,-0.21};
//Rs   x[0]
//Rsh  x[1]
//IPh  x[2]	
//Isd  x[3]	
//n    x[4]
double q=1.602176565e-19;// electronic charge
double kb=1.3806505e-23;//Boltzmann
double T=273.15+33.0;

int m=Vt.length;
double sum=0.0;


for(int i=0;i<m;i++)
{sum+=(It[i]-x[2]+(x[3]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[4]*kb*T))-1.0))+((Vt[i]+x[0]*It[i])/(x[1])))*(It[i]-x[2]+(x[3]*(Math.exp(q*(Vt[i]+x[0]*It[i])/(x[4]*kb*T))-1.0))+((Vt[i]+x[0]*It[i])/(x[1])));}
	
return Math.sqrt(sum/(double)m);	
}	
}


class pv extends f_xj// Himmelblau f(x)=0.0  @x=(3.0,2.0),(-2.8051,3.1313),(-3.7793,-3.2831),(3.5844,-1.8481)   -6.0<=x[i]<=6.0 i=0,1
{
double func(double x[])
{
// double  diode	
double[] Vt={0.1248,1.8093,3.3511,4.7622,6.0538,7.2364,8.3189,9.3097,10.2163,11.0449,11.8018,12.4929,13.1231,13.6983,14.2221,14.6995,15.1346,15.5311,15.8929,16.2229,16.5241,16.7987,17.0499,17.2793,17.4885};
double[] It={1.0315,1.0300,1.0260,1.0220,1.0180,1.0155,1.0140,1.0100,1.0035,0.9880,0.9630,0.9255,0.8725,0.8075,0.7265,0.6345,0.5345,0.4275,0.3185,0.2085,0.1010,-0.0080,-0.1110,-0.2090,-0.3030 };
//Rs    x[0]
//Rsh   x[1]
//IPh   x[2]	
//Isd1  x[3]	
//n1    x[4]

double q=1.602176565e-19;// electronic charge
double kb=1.3806505e-23;//Boltzmann
double T=273.15+45.0;
double Np=1.0;
double Ns=36.0;
double Vtt=kb*T/q;

int m=Vt.length;
double sum=0.0;


for(int i=0;i<m;i++)
{sum+=(It[i]-x[2]+(x[3]*(Math.exp((Vt[i]+It[i]*x[0])/(x[4]*Vtt*Ns))-1.0))+(((Vt[i]+It[i]*x[0])/(x[1]))))*(It[i]-x[2]+(x[3]*(Math.exp((Vt[i]+It[i]*x[0])/(x[4]*Vtt*Ns))-1.0))+(((Vt[i]+It[i]*x[0])/(x[1]))))    ;}
	
return Math.sqrt(sum/(double)m);	
}	
}

class f2001 extends f_xj //optimal chiller case 1 coelho
{
	
double[] RT={1280.0,1280.0,1280.0,1280.0,1250.0,1250.0};
double CL=5334.0;

public double func(double x[]) 
{
double P[]=new double[6]; 	
double a[]={399.345,287.116,-120.505,-19.121,-95.029,191.750};
double b[]={-122.12,80.04,1525.99,898.76,1202.39,224.86};	
double c[]={770.46,700.48,-502.14,-98.15,-352.16,524.04};


double sum=0.0;
for(int i=0;i<6;i++)
{P[i]=a[i]+b[i]*x[i]+c[i]*x[i]*x[i];}
for(int i=0;i<6;i++)
{sum+=P[i];}
return sum+5.0*H(x);
}

double H(double x[])
{
double sum1=0.0;	
for(int i=0;i<6;i++)
{sum1+=x[i]*RT[i];}
return Math.abs(sum1-CL)-1e-8;
}
}


class f2002 extends f_xj //optimal chiller case 2 coelho
{
double RT=800.0;
double CL=960.0;

public double func(double x[]) 
{
double P[]=new double[3]; 	
double a[]={100.95,66.598,130.09};
double b[]={818.61,606.34,304.5};	
double c[]={-973.43,-380.58,14.377};
double d[]={788.55,275.95,99.8};


double sum=0.0;
for(int i=0;i<3;i++)
{P[i]=a[i]+b[i]*x[i]+c[i]*x[i]*x[i]+d[i]*x[i]*x[i]*x[i];}
for(int i=0;i<3;i++)
{sum+=P[i];}
return sum+5.0*H(x);
}

double H(double x[])
{
double sum1=0.0;	
for(int i=0;i<3;i++)
{sum1+=x[i]*RT;}
return Math.abs(sum1-CL)-1e-8;
}
}

class f2003 extends f_xj //optimal chiller case 3 genetic chang 
{
double[] RT={450.0,450,1000.0,1000.0};
double CL=1450.0;

public double func(double x[]) 
{
double P[]=new double[4]; 	
double a[]={104.09,-67.15,384.71,541.63};
double b[]={166.57,1177.79,-779.13,413.48};	
double c[]={-430.13,-2174.53,1151.42,-3626.50};
double d[]={512.53,1456.53,-63.20,4021.41};


double sum=0.0;
for(int i=0;i<4;i++)
{P[i]=a[i]+b[i]*x[i]+c[i]*x[i]*x[i]+d[i]*x[i]*x[i]*x[i];}
for(int i=0;i<4;i++)
{sum+=P[i];}
return sum+5.0*H(x);
}

double H(double x[])
{
double sum1=0.0;	
for(int i=0;i<4;i++)
{sum1+=x[i]*RT[i];}
return Math.abs(sum1-CL)-1e-8;
}
}


class dam2 extends f_xj
{
  
 

double func(double x[])
{
// x[0]  x[1]  x[2]  x[3]  x[4]  x[5]  x[6]  x[7]  x[8]  x[9]  x[10]  x[11]  x[12]  x[13]  x[14]  x[15]  x[16]  x[17]  x[18]  x[19]  x[20]  x[21]   x[22]   x[23] 
// R1    R2    R3    R4    R5    R6    R7    R8    R9    R10   R11    R12    S1     S2     S3     S4     S5     S6     S7     S8     S9     S10     S11     S12

double[] irrigation_demand={0.0, 0.0, 3.28,  5.36,  3.0, 3.22, 3.22, 1.24, 8.06,  7.65,  7.65,  4.71 };
//                          Jan, Feb, March, Aprl,  May, June, July, Agst, Sptmr, Octbr, Nvmbr, Dcmbr
double[] deficit=new double[12];

for(int i=0;i<12;i++)
{deficit[i]=irrigation_demand[i]-x[i];}
 
double sum=0.0;
for(int i=0;i<12;i++)
{sum+= deficit[i]*deficit[i];}
return sum+(300000000007.38*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)+H8(x)+H9(x)+H10(x)+H11(x)+H12(x)))    ;
}	

double H1(double x[])
{
	
double St1=0.0;
double St2=0.0;
 
St1=x[12];
St2=x[13];

double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78}; 

double 	A0=0.002*St1*St1+0.3446*St1+2.6533;//m^2	
double Evap0=A0*evap_rate[0];
double hh1=St2-(St1+I_med[0]-x[0]-Evap0);
return Math.abs(hh1);
}

double H2(double x[])
{
 
 
double St2=x[13];
double St3=x[14];
 
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 

double 	A1=0.002*St2*St2+0.3446*St2+2.6533;//m^2		
double Evap1=A1*evap_rate[1];
double hh2=St3-(St2+I_med[1]-x[1]-Evap1);
return Math.abs(hh2);
}
	
double H3(double x[])
{
double St3=x[14];
double St4=x[15];	
	
 
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78}; 

double 	A2=0.002*St3*St3+0.3446*St3+2.6533;//m^2		
double Evap2=A2*evap_rate[2];
double hh3=St4-(St3+I_med[2]-x[2]-Evap2 );
return Math.abs(hh3);
}	

double H4(double x[])
{

double St4=x[15];
double St5=x[16];		

 	
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
double 	A3=0.002*St4*St4+0.3446*St4+2.6533;//m^2	
double Evap3=A3*evap_rate[3];
double hh4=St5-(St4+I_med[3]-x[3]-Evap3);
return Math.abs(hh4);
}	

double H5(double x[])
{
	
double St5=x[16];
double St6=x[17];	
	
 
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
 
double 	A4=0.002*St5*St5+0.3446*St5+2.6533;//m^2	
double Evap4=A4*evap_rate[4];
double hh5=St6-(St5+I_med[4]-x[4]-Evap4 );
return Math.abs(hh5);
}


double H6(double x[])
{

double St6=x[17];
double St7=x[18];		
 	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
  
double 	A5=0.002*St6*St6+0.3446*St6+2.6533;//m^2	
double Evap5=A5*evap_rate[5];
double hh6=St7-(St6+I_med[5]-x[5]-Evap5 );
return Math.abs(hh6);
}


double H7(double x[])
{
	
double St7=x[18];
double St8=x[19];		
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
double 	A6=0.002*St7*St7+0.3446*St7+2.6533;//m^2	
double Evap6=A6*evap_rate[6];
double hh7=St8-(St7+I_med[6]-x[6]-Evap6 );
return Math.abs(hh7);
}



double H8(double x[])
{
double St8=x[19];
double St9=x[20];		
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
double 	A7=0.002*St8*St8+0.3446*St8+2.6533;//m^2	
double Evap7=A7*evap_rate[7];
double hh8=St9-(St8+I_med[7]-x[7]-Evap7 );
return Math.abs(hh8);
}


double H9(double x[])
{
	
double St9=x[20];
double St10=x[21];			
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
double 	A8=0.002*St9*St9+0.3446*St9+2.6533;//m^2	
double Evap8=A8*evap_rate[8];
double hh9=St10-(St9+I_med[8]-x[8]-Evap8 );
return Math.abs(hh9);
}


double H10(double x[])
{
	
double St10=x[21];
double St11=x[22];		
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
double 	A9=0.002*St10*St10+0.3446*St10+2.6533;//m^2	
double Evap9=A9*evap_rate[9];
double hh10=St11-(St10+I_med[9]-x[9]-Evap9);
return Math.abs(hh10);
}


double H11(double x[])
{

double St11=x[22];
double St12=x[23];	
		
	
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
 
double 	A10=0.002*St11*St11+0.3446*St11+2.6533;//m^2		
double Evap10=A10*evap_rate[10] ;
double hh11=St12-(St11+I_med[10]-x[10]-Evap10);
return Math.abs(hh11);
}

	
double H12(double x[])
{
	
double St12=x[23];
double St1=x[12];
 
double evap_rate[]={0.115, 0.1223, 0.1336, 0.1161, 0.1083, 0.0938, 0.0963, 0.0972, 0.0928, 0.0862, 0.0814, 0.0846 }; // m/month
double[]   I_med={ 4.17, 2.03, 3.79, 3.43, 3.37, 3.26,  5.71, 6.31,5.6, 14.87, 18.17, 12.78};
 
double 	A11=0.002*St12*St12+0.3446*St12+2.6533;//m^2		
double Evap11=A11*evap_rate[11];
double hh12=St1-(St12+I_med[11]-x[11]-Evap11);
return Math.abs(hh12);
}


 



 


}






class cec_g01 extends f_xj
{
 double func(double x[])
{
double top1=0.0;
double top2=0.0;
double top3=0.0;
for(int i=0;i<4;i++)
{top1+=x[i];}
for(int i=0;i<4;i++)
{top2+=x[i]*x[i];}
for(int i=4;i<13;i++)
{top3+=x[i];}
double fnc=5.0*top1-5.0*top2-top3;

double mu=400.0;
return fnc+mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)+G6(x)+G7(x)+G8(x)+G9(x));
}	

 

double G1(double x[])
{return Math.pow(Math.max(0.0,    2.0*x[0]+2.0*x[1]+x[9]+x[10]-10.0       ),2.0);  }

double G2(double x[])
{return Math.pow(Math.max(0.0,    2.0*x[0]+2.0*x[2]+x[9]+x[11]-10.0       ),2.0);  }

double G3(double x[])
{return Math.pow(Math.max(0.0,    2.0*x[1]+2.0*x[2]+x[10]+x[11]-10.0       ),2.0);  }

double G4(double x[])
{return Math.pow(Math.max(0.0,    -8.0*x[0]+x[9]      ),2.0);  }

double G5(double x[])
{return Math.pow(Math.max(0.0,    -8.0*x[1]+x[10]      ),2.0);  }

double G6(double x[])
{return Math.pow(Math.max(0.0,    -8.0*x[2]+x[11]      ),2.0);  }

double G7(double x[])
{return Math.pow(Math.max(0.0,    -2.0*x[3]-x[4]+x[9]     ),2.0);  }

double G8(double x[])
{return Math.pow(Math.max(0.0,      -2.0*x[5]-x[6]+x[10]     ),2.0);  }

double G9(double x[])
{return Math.pow(Math.max(0.0,      -2.0*x[7]-x[8]+x[11]     ),2.0);  }
 
}



class cec_g02 extends f_xj
{
 double func(double x[])
{
int DD=x.length;	
double top1=0.0;
double top2=1.0;
double top3=0.0;
for(int i=0;i<DD;i++)
{top1+= Math.pow(Math.cos(x[i]),4.0);}

for(int i=0;i<DD;i++)
{top2*=Math.pow(Math.cos(x[i]),2.0);}

for(int i=0;i<DD;i++)
{top3+=(double)(i+1)*x[i]*x[i];}

double fncc=-Math.abs((top1-2.0*top2)/Math.sqrt(top3));

 

double mu=80000.0;
return fncc+mu*(G1(x)+G2(x));
}	

 

double G1(double x[])
{
	double sum=1.0;
	for(int i=0;i<x.length;i++)
	{sum*=x[i];}
	
	return Math.pow(Math.max(0.0,    0.75-sum       ),2.0);  
}

double G2(double x[])
{
	int DD1=x.length;
	double sum1=0.0;
	for(int i=0;i<x.length;i++)
	{sum1+=x[i];}
	return Math.pow(Math.max(0.0,   sum1-(double)DD1 *7.5      ),2.0);  
}

  
}




class cec_g03 extends f_xj
{
 double func(double x[])
{
int DD=x.length;	
 
double top1=1.0;
 
for(int i=0;i<DD;i++)
{top1*= x[i];}

double fncc=-Math.pow(Math.sqrt((double)DD),(double)DD)*top1; 
  

double mu=6000.0;
return fncc+mu*(G1(x));
}	

 

double G1(double x[])
{
	double sum=0.0;
	for(int i=0;i<x.length;i++)
	{sum+=x[i]*x[i];}
	
	return Math.abs(sum-1.0);  
}

}



class cec_g04 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=357000000000000.95989;
return (5.3578547*x[2]*x[2])+(0.8356891*x[0]*x[4])+(37.293239*x[0])-(40792.141)+(mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)+G6(x)));
}

double G1(double x[])
{

return Math.pow(Math.max(0.0,85.334407+0.0056858*x[1]*x[4]+0.0006262*x[0]*x[3]-0.0022053*x[2]*x[4]-92.0),2.0);	

}
double G2(double x[])
{
return Math.pow(Math.max(0.0,-85.334407-0.0056858*x[1]*x[4]-0.0006262*x[0]*x[3]+0.0022053*x[2]*x[4]),2.0);	
}
double G3(double x[])
{
return Math.pow(Math.max(0.0,80.51249+0.0071317*x[1]*x[4]+0.0029955*x[0]*x[1]+0.0021813*x[2]*x[2]-110.0),2.0);	
}
double G4(double x[])
{
return Math.pow(Math.max(0.0,-80.51249-0.0071317*x[1]*x[4]-0.0029955*x[0]*x[1]-0.0021813*x[2]*x[2]+90.0),2.0);	
}
double G5(double x[])
{
return Math.pow(Math.max(0.0,9.300961+0.0047026*x[2]*x[4]+0.0012547*x[0]*x[2]+0.0019085*x[2]*x[3]-25.0),2.0);	
}
double G6(double x[])
{
return Math.pow(Math.max(0.0,-9.300961-0.0047026*x[2]*x[4]-0.0012547*x[0]*x[2]-0.0019085*x[2]*x[3]+20.0),2.0);	
}
}



 


class cec_g05 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1000.2;
double s1=((3.0*x[0])+(1e-6*x[0]*x[0]*x[0])+(2.0*x[1])+((2e-6/3.0)*x[1]*x[1]*x[1]))+mu*(G1(x)+G2(x)+H1(x)+H2(x)+H3(x));
return s1;
}

double G1(double x[])
{
return  Math.pow(Math.max(0.0,x[2]-x[3]-0.55),2.0);	
}

double G2(double x[])
{
return Math.pow(Math.max(0.0,x[3]-x[2]-0.55),2.0);		
}

double H1(double x[])
{
return Math.abs(1000.0*(Math.sin(-x[2]-0.25)+Math.sin(-x[3]-0.25))+894.8-x[0])-1e-8;	
}

double H2(double x[])
{
return Math.abs(1000.0*(Math.sin(x[2]-0.25)+Math.sin(x[2]-x[3]-0.25))+894.8-x[1])-1e-8;	
}

double H3(double x[])
{
return Math.abs(1000.0*(Math.sin(x[3]-0.25)+Math.sin(x[3]-x[2]-0.25))+1294.8)-1e-8;	
}
}



class cec_g06 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1000000000000.2;
double s1= (x[0]-10.0)*(x[0]-10.0)*(x[0]-10.0)+(x[1]-20.0)*(x[1]-20.0)*(x[1]-20.0)+mu*(G1(x)+G2(x));
return s1;
}

double G1(double x[])
{
return  Math.pow(Math.max(0.0,-(x[0]-5.0)*(x[0]-5.0)-(x[1]-5.0)*(x[1]-5.0)+100.0),2.0);	
}

double G2(double x[])
{
return Math.pow(Math.max(0.0,(x[0]-6.0)*(x[0]-6.0)+(x[1]-5.0)*(x[1]-5.0)-82.81),2.0);		
}

 }

class cec_g07 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=10000.00007;
double fnc=(x[0]*x[0])+(x[1]*x[1])+(x[0]*x[1])-(14.0*x[0])-(16.0*x[1])+((x[2]-10.0)*(x[2]-10.0))+(4.0*(x[3]-5.0)*(x[3]-5.0))+((x[4]-3.0)*(x[4]-3.0))+(2.0*(x[5]-1.0)*(x[5]-1.0))+(5.0*x[6]*x[6])+(7.0*(x[7]-11.0)*(x[7]-11.0))+(2.0*(x[8]-10.0)*(x[8]-10.0))+((x[9]-7.0)*(x[9]-7.0))+45.0;
return fnc+mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)+G6(x)+G7(x)+G8(x));
}		
	
double G1(double x[])
{return Math.pow(Math.max(0.0,     -105.0+4.0*x[0]+5.0*x[1]-3.0*x[6]+9.0*x[7]       ),2.0);  }

double G2(double x[])
{return Math.pow(Math.max(0.0,     10.0*x[0]-8*x[1]-17*x[6]+2.0*x[7]       ),2.0);  }	
	
double G3(double x[])
{return Math.pow(Math.max(0.0,     -8.0*x[0]+2.0*x[1]+5*x[8]-2.0*x[9]-12.0       ),2.0);  }	
	
double G4(double x[])
{return Math.pow(Math.max(0.0,     (3.0*(x[0]-2.0)*(x[0]-2.0))+(4.0*(x[1]-3.0)*(x[1]-3.0))+2.0*x[2]*x[2]-7.0*x[3]-120.0       ),2.0);  }	
	
double G5(double x[])
{return Math.pow(Math.max(0.0,     5.0*x[0]*x[0]+8.0*x[1]+((x[2]-6.0)*(x[2]-6.0))-2.0*x[3]-40.0     ),2.0);  }		
	
double G6(double x[])
{return Math.pow(Math.max(0.0,     (x[0]*x[0])+(2.0*(x[1]-2.0)*(x[1]-2.0))-(2.0*x[0]*x[1])+14.0*x[4]-6.0*x[5]    ),2.0);  }	

double G7(double x[])
{return Math.pow(Math.max(0.0,     (0.5*(x[0]-8.0)*(x[0]-8.0))+(2.0*(x[1]-4.0)*(x[1]-4.0))+3.0*x[4]*x[4]-x[5]-30.0       ),2.0);  }	

double G8(double x[])
{return Math.pow(Math.max(0.0, -3.0*x[0]+6.0*x[1]+(12.0*(x[8]-8.0)*(x[8]-8.0))-7.0*x[9]            ),2.0);  }	
}


 

class cec_g08 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiy	
double mu=100.00007;
double s1=-Math.sin(2.0*Math.PI*x[0])*Math.sin(2.0*Math.PI*x[0])*Math.sin(2.0*Math.PI*x[0])*Math.sin(2.0*Math.PI*x[1])/(x[0]*x[0]*x[0]*(x[0]+x[1]));
 
return s1+mu*(H1(x)+H2(x));
}	
	
double H1(double x[])
{return Math.pow(Math.max(0.0,     x[0]*x[0]-x[1]+1.0       ),2.0);                                       }	
	
double H2(double x[])
{return Math.pow(Math.max(0.0,    1.0-x[0]+((x[1]-4.0)*(x[1]-4.0))         ),2.0); }	
}



class cec_g09 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1000000000.25;
return ((x[0]-10.0)*(x[0]-10.0))+(5.0*(x[1]-12.0)*(x[1]-12.0))+(x[2]*x[2]*x[2]*x[2])+(3.0*(x[3]-11.0)*(x[3]-11.0))+(10.0*(x[4]*x[4]*x[4]*x[4]*x[4]*x[4]))+(7.0*x[5]*x[5])+(x[6]*x[6]*x[6]*x[6])-(4.0*x[5]*x[6])-10*x[5]-8.0*x[6]+mu*(G1(x)+G2(x)+G3(x)+G4(x));
}

double G1(double x[])
{
return Math.pow(Math.min(0.0,127.0-2.0*x[0]*x[0]-3.0*x[1]*x[1]*x[1]*x[1]-x[2]-4.0*x[3]*x[3]-5.0*x[4]),2.0);	
}
double G2(double x[])
{
return Math.pow(Math.min(0.0,282.0-7.0*x[0]-3.0*x[1]-10.0*x[2]*x[2]-x[3]+x[4]),2.0);	
}
double G3(double x[])
{
return Math.pow(Math.min(0.0,196.0-23.0*x[0]-x[1]*x[1]-6.0*x[5]*x[5]+8.0*x[6]),2.0);	
}
double G4(double x[])
{
return Math.pow(Math.min(0.0,-4.0*x[0]*x[0]-x[1]*x[1]+3.0*x[0]*x[1]-2.0*x[2]*x[2]-5.0*x[5]+11.0*x[6]),2.0);	
}
}


class cec_g10 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=10000000000.00007;

return x[0]+x[1]+x[2]+mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x));
}	
	
double H1(double x[])
{return Math.pow(Math.max(0.0,      -1.0+0.0025*(x[3]+x[5])       ),2.0);                                       }	
	
double H2(double x[])
{return Math.pow(Math.max(0.0,    -1.0+0.0025*(x[4]+x[6]-x[3])         ),2.0); }	
	
double H3(double x[])
{return Math.pow(Math.max(0.0,      -1.0+0.01*(x[7]-x[4])      ),2.0); }	
	
double H4(double x[])
{return Math.pow(Math.max(0.0,      -x[0]*x[5]+833.33252*x[3]+100.0*x[0]-83333.333       ),2.0);   }	

double H5(double x[])
{return Math.pow(Math.max(0.0,      -x[1]*x[6]+1250.0*x[4]+x[1]*x[3]-1250.0*x[3]       ),2.0);   }

double H6(double x[])
{return Math.pow(Math.max(0.0,      -x[2]*x[7]+1250000.0+x[2]*x[4]-2500.0*x[4]       ),2.0);   }
}
 

class cec_g11 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=5.0;
double s1=x[0]*x[0]+(x[1]-1.0)*(x[1]-1.0)+mu*(H1(x));
return s1;
}

double H1(double x[])
{
return Math.abs(x[1]-x[0]*x[0])-1e-8;
}
}



class cec_g13 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.07;
double s1=Math.exp(x[0]*x[1]*x[2]*x[3]*x[4])+mu*(H1(x)+H2(x)+H3(x));
return s1;
}

double H1(double x[])
{
  int n=x.length;
  double d=0.0;
  for(int i=0;i<n;i++)
  {d+=x[i]*x[i];}
  return Math.abs(d-10.0)-1e-8; 	
	
}

double H2(double x[])
{
return  Math.abs(x[1]*x[2]-5.0*x[3]*x[4])-1e-8; 	
}

double H3(double x[])
{
return Math.abs(x[0]*x[0]*x[0]+x[1]*x[1]*x[1]+1.0)-1e-8;	
}

}



class cec_g14 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double[] c={-6.089,-17.164, -34.054, -5.914,-24.721,-14.986,-24.1,-10.708,-26.662, -22.719};
double mu=90.07;
double sum=0.0;

for(int i=0;i<10;i++)
{sum+=x[i]*(c[i]+Math.log(x[i]/(x[0]+x[1]+x[2]+x[3]+x[4]+x[5]+x[6]+x[7]+x[8]+x[9])));}


double s1=sum+mu*(H1(x)+H2(x)+H3(x));
return s1;
}

double H1(double x[])
{
 
  return Math.abs( x[0]+2.0*x[1]+2.0*x[2]+x[5]+x[9]-2.0 ); 	
	
}

double H2(double x[])
{
return  Math.abs( x[3]+2.0*x[4]+x[5]+x[6]-1.0 ); 	
}

double H3(double x[])
{
return Math.abs( x[2]+x[6]+x[7]+2.0*x[8]+x[9]-1.0  )-1e-8;	
}

}



class cec_g15 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
 double mu=90.07;
 double fx=1000-x[0]*x[0]-2.0*x[1]*x[1]-x[2]*x[2]-x[0]*x[1]-x[0]*x[2];

double s1=fx+mu*(H1(x)+H2(x) );
return s1;
}

double H1(double x[])
{
 
  return Math.abs( x[0]*x[0]+x[1]*x[1]+x[2]*x[2]-25.0   ); 	
	
}

double H2(double x[])
{
return  Math.abs( 8.0*x[0]+14.0*x[1]+7.0*x[2]-56.0 ); 	
}

 

}



class cec_g17 extends f_xj          
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
 double mu=200.07;
double f1=0.0;
double f2=0.0;

 if((x[0]>=0.0)&&(x[0]<300.0))
 {f1=30.0*x[0];}
 else if ((x[0]>=300.0)&&(x[0]<400.0))
 {f1=31.0*x[0];}
 
 if((x[1]>=0.0)&&(x[1]<100.0))
 {f2=28.0*x[1];}
 else if ((x[1]>=100.0)&&(x[1]<200.0))
 {f2=29.0*x[1];} 
 else if ((x[1]>=200.0)&&(x[1]<1000.0))
 {f2=30.0*x[1];}
 
 
double s1=f1+f2+mu*(H1(x)+H2(x)+H3(x)+H4(x));
return s1;
}

double H1(double x[])
{
  double s1=-x[0]+300.0- (x[2]*x[3]*Math.cos(1.48477-x[5])/131.078)+ (0.90798*x[2]*x[2]*Math.cos(1.47588)/131.078);
  
  
  return Math.abs( s1   ); 	
	
}

double H2(double x[])
{
  double s2=-x[1]- (x[2]*x[3]*Math.cos(1.48477+x[5])/131.078)+ (0.90798*x[3]*x[3]*Math.cos(1.47588)/131.078);
	
	
return  Math.abs( s2 ); 	
}

 
double H3(double x[])
{
  double s3=-x[4]- (x[2]*x[3]*Math.sin(1.48477+x[5])/131.078)+ (0.90798*x[3]*x[3]*Math.sin(1.47588)/131.078);
	
	
return  Math.abs( s3 ); 	
}

double H4(double x[])
{
  double s4=200.0- (x[2]*x[3]*Math.sin(1.48477-x[5])/131.078)+ (0.90798*x[2]*x[2]*Math.sin(1.47588)/131.078);
	
	
return  Math.abs( s4 ); 	
}


}



 


class cec_g18 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1000000000000.00007;

return   -0.5*(x[0]*x[3]-x[1]*x[2]+x[2]*x[8]-x[4]*x[8]+x[4]*x[7]-x[5]*x[6])+mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)+H8(x)+H9(x)+H10(x)+H11(x)+H12(x)+H13(x));
}	
	
double H1(double x[])
{return Math.pow(Math.max(0.0,     x[2]*x[2]+x[3]*x[3]-1.0       ),2.0);                                       }	
	
double H2(double x[])
{return Math.pow(Math.max(0.0,    x[8]*x[8]-1.0         ),2.0); }	
	
double H3(double x[])
{return Math.pow(Math.max(0.0,      x[4]*x[4]+x[5]*x[5]-1.0      ),2.0); }	
	
double H4(double x[])
{return Math.pow(Math.max(0.0,      x[0]*x[0]+(x[1]-x[8])*(x[1]-x[8])-1.0      ),2.0);   }	

double H5(double x[])
{return Math.pow(Math.max(0.0,      ((x[0]-x[4])*(x[0]-x[4]))+((x[1]-x[5])*(x[1]-x[5]))-1.0      ),2.0);   }

double H6(double x[])
{return Math.pow(Math.max(0.0,      ((x[0]-x[6])*(x[0]-x[6]))+((x[1]-x[7])*(x[1]-x[7]))-1.0       ),2.0);   }

double H7(double x[])
{return Math.pow(Math.max(0.0,      ((x[2]-x[4])*(x[2]-x[4]))+((x[3]-x[5])*(x[3]-x[5]))-1.0       ),2.0);   }

double H8(double x[])
{return Math.pow(Math.max(0.0,      ((x[2]-x[6])*(x[2]-x[6]))+((x[3]-x[7])*(x[3]-x[7]))-1.0       ),2.0);   }

double H9(double x[])
{return Math.pow(Math.max(0.0,      ((x[6])*(x[6]))+((x[7]-x[8])*(x[7]-x[8]))-1.0        ),2.0);   }

double H10(double x[])
{return Math.pow(Math.max(0.0,        x[1]*x[2]-x[0]*x[3]   ),2.0);   }

double H11(double x[])
{return Math.pow(Math.max(0.0,      -x[2]*x[8]       ),2.0);   }

double H12(double x[])
{return Math.pow(Math.max(0.0,      x[4]*x[8]       ),2.0);   }

double H13(double x[])
{return Math.pow(Math.max(0.0,      x[5]*x[6]-x[4]*x[7]      ),2.0);   }

}


class cec_g21 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100000000000000000000000.00007;

return  x[0]+mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+G1(x));
}	
	
double G1(double x[])
{return Math.pow(Math.max(0.0,     -x[0]+35.0*Math.pow(x[1],0.6)+35.0*Math.pow(x[2],0.6)       ),2.0);                                       }	
	
double H1(double x[])
{return Math.abs( -300.0*x[2]+7500*x[4]-7500*x[5]-25.0*x[3]*x[4]+25.0*x[3]*x[5] + x[2]*x[3]               ); }	
	
double H2(double x[])
{return Math.abs(100.0*x[1]+155.365*x[3]+2500.0*x[6]-x[1]*x[3]-25.0*x[3]*x[6]-15536.5); }	
	
double H3(double x[])
{return Math.abs(-x[4]+Math.log(-x[3]+900.0));   }	

double H4(double x[])
{return Math.abs(-x[5]+Math.log(x[3]+300.0));   }

double H5(double x[])
{return   Math.abs(-x[6]+Math.log(-2.0*x[3]+700.0));   }

 
}


class cec_g22 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100000000000000000000000.00007;

return  x[0]+mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x));
}	
	
double G1(double x[])
{return Math.pow(Math.max(0.0,     -x[0]+Math.pow(x[1],0.6)+Math.pow(x[2],0.6)+Math.pow(x[3],0.6)       ),2.0);                                       }	
	
double H1(double x[])
{return Math.abs(  x[4]- 100000*x[7]+10000000            ); }	
	
double H2(double x[])
{return Math.abs( x[5]+100000*x[7]-  100000*x[8]     ); }	
	
double H3(double x[])
{return Math.abs( x[6]+ 100000*x[8] -50000000.0  );   }	

double H4(double x[])
{return Math.abs( x[4]+100000*x[9]-3.3e7   );   }

double H5(double x[])
{return   Math.abs( x[5]+100000*x[10]-4.4e7    );   }

double H6(double x[])
{return   Math.abs( x[6]+100000*x[11]-6.6e7    );   }

double H7(double x[])
{return   Math.abs( x[4]-120.0*x[1]*x[12]    );   } 

double H8(double x[])
{return   Math.abs( x[5]-80.0*x[2]*x[13]    );   } 

double H9(double x[])
{return   Math.abs( x[6]-40.0*x[3]*x[14]    );   } 

double H10(double x[])
{return   Math.abs( x[7]-x[10]+x[15]    );   }

double H11(double x[])
{return   Math.abs( x[8]-x[11]+x[16]    );   }

double H12(double x[])
{return   Math.abs(  -x[17]+Math.log( x[9]-100)       );   }

double H13(double x[])
{return   Math.abs(  -x[18]+Math.log( -x[7]+300)       );   }

double H14(double x[])
{return   Math.abs(  -x[19]+Math.log(x[15])       );   }

double H15(double x[])
{return   Math.abs(  -x[20]+Math.log(-x[8]+400)       );   }

double H16(double x[])
{return   Math.abs(  -x[21]+Math.log(x[16])       );   }

double H17(double x[])
{return   Math.abs(  -x[7]-x[9]+x[12]*x[17]-x[12]*x[18]+400.0       );   }

double H18(double x[])
{return   Math.abs(  x[7]-x[8]-x[10]+x[13]*x[19]-x[13]*x[20]+400.0       );   }

double H19(double x[])
{return   Math.abs(  x[8]-x[11]-4.60517*x[14]+x[14]*x[21]+100.0       );   }

}



class cec_g23 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return  -9.0*x[4]-15.0*x[7]+6.0*x[0]+16.0*x[1]+10.0*(x[5]+x[6])+mu*(G1(x)+G2(x)+H1(x)+H2(x)+H3(x)+H4(x));
}	
	
double G1(double x[])
{return Math.pow(Math.max(0.0,       x[8]*x[2]+0.02*x[5]-0.025*x[4]      ),2.0);                                       }	
	
double G2(double x[])
{return Math.pow(Math.max(0.0,       x[8]*x[3]+0.02*x[6]-0.015*x[7]      ),2.0); }	
	
double H1(double x[])
{return Math.abs( x[0]+x[1]-x[2]-x[3]     ); }	
	
double H2(double x[])
{return Math.abs(  0.03*x[0]+0.01*x[1]-x[8]*(x[2]+x[3])  );   }	

double H3(double x[])
{return Math.abs( x[2]+x[5]-x[4]   );   }

double H4(double x[])
{return   Math.abs( x[3]+x[6]-x[7]   );   }

}



class cec_g24 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100000000000.00007;

return  -x[0]-x[1]+mu*(G1(x)+G2(x) );
}	
	
double G1(double x[])
{return Math.pow(Math.max(0.0,       -2.0*x[0]*x[0]*x[0]*x[0]+8.0*x[0]*x[0]*x[0]-8.0*x[0]*x[0]+x[1]-2.0     ),2.0);                                       }	
	
double G2(double x[])
{return Math.pow(Math.max(0.0,        -4.0*x[0]*x[0]*x[0]*x[0]+32.0*x[0]*x[0]*x[0]-88.0*x[0]*x[0]+96.0*x[0]+x[1]-36.0      ),2.0); }	
}


class threebartruss extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100000000000000000000.100000000;
double s1=((2.0*Math.sqrt(2.0)*x[0]+x[1])*100)+mu*(H1(x)+H2(x)+H3(x));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0, (((Math.sqrt(2.0)*x[0]+x[1])/(Math.sqrt(2)*x[0]*x[0]+2.0*x[0]*x[1]))*2.0)-2.0  ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,    2.0*(x[1]/(Math.sqrt(2)*x[0]*x[0]+2.0*x[0]*x[1]))-2.0),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,2.0*(1.0/(Math.sqrt(2.0)*x[1]+x[0]))-2.0),2.0);}
}


//Tension/compression spring design problem
class tensioncompression extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100000000000.3;
double s1=((x[2]+2.0)*x[1]*x[0]*x[0])+(mu*(H1(x)+H2(x)+H3(x)+H4(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         1.0-((x[1]*x[1]*x[1]*x[2])/(71785.0*x[0]*x[0]*x[0]*x[0]))                  ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,        (((4.0*x[1]*x[1])-(x[0]*x[1]))/(12566.0*(x[1]*x[0]*x[0]*x[0]-x[0]*x[0]*x[0]*x[0])))+(1.0/(5108.0*x[0]*x[0]))-1.0              ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,        1.0-((140.45*x[0])/(x[1]*x[1]*x[2]))        ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,     ((x[0]+x[1])/1.5)-1.0        ),2.0);}
}


// speed reducer design problem
class speedreducer extends f_xj         
{
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=8000000.7;
double s1=0.7854*x[0]*x[1]*x[1]*(3.3333*x[2]*x[2]+14.9334*x[2]-43.0934)-(1.508*x[0]*(x[5]*x[5]+x[6]*x[6]))+(7.4777*(x[5]*x[5]*x[5]+x[6]*x[6]*x[6]))+(0.7854*(x[3]*x[5]*x[5]+x[4]*x[6]*x[6]))+(mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)+H8(x)+H9(x)+H10(x)+H11(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         (27.0/(x[0]*x[1]*x[1]*x[2]))-1.0                 ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,       (397.5/(x[0]*x[1]*x[1]*x[2]*x[2]))-1.0             ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,      ((1.93*x[3]*x[3]*x[3])/(x[1]*x[2]*x[5]*x[5]*x[5]*x[5]))-1.0          ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,     ((1.93*x[4]*x[4]*x[4])/(x[1]*x[6]*x[6]*x[6]*x[6]*x[2]))-1.0        ),2.0);}

double H5(double x[])
{return  Math.pow(Math.max(0.0,     (Math.sqrt((745.0*(x[3]/(x[1]*x[2])))*(745.0*(x[3]/(x[1]*x[2])))+16.9e6)/(110.0*x[5]*x[5]*x[5]))-1.0                                    ),2.0);}

double H6(double x[])
{return  Math.pow(Math.max(0.0,    (Math.sqrt((745.0*(x[4]/(x[1]*x[2])))*(745.0*(x[4]/(x[1]*x[2])))+157.5e6)/(85.0*x[6]*x[6]*x[6]))-1.0     ),2.0);}

double H7(double x[])
{return  Math.pow(Math.max(0.0,     x[1]*x[2]-40.0       ),2.0);}

double H8(double x[])
{return  Math.pow(Math.max(0.0,     -x[0]+5.0*x[1]       ),2.0);}

double H9(double x[])
{return  Math.pow(Math.max(0.0,     x[0]-12.0*x[1]       ),2.0);}


double H10(double x[])
{return  Math.pow(Math.max(0.0,     1.5*x[5]+1.9-x[3]       ),2.0);}

double H11(double x[])
{return  Math.pow(Math.max(0.0,     1.1*x[6]+1.9-x[4]       ),2.0);}
 
}


class weldedbeam extends f_xj         
{
	
double P=6000.0;
double L=14.0;
double E=30000000.0;	
double G=12000000.0;
double tomax=13600.0;
double sigmax=30000.0;
double deltamax=0.25;

double sigma(double x[])
{return (6.0*P*L)/(x[3]*x[2]*x[2]);}

double delta(double x[])
{return (4.0*P*L*L*L)/(E*x[2]*x[2]*x[2]*x[3]);}

double Pc(double x[])
{return (4.013*Math.sqrt(E*G*x[2]*x[2]*x[3]*x[3]*x[3]*x[3]*x[3]*x[3]/36.0)/L/L)*(1.0-((x[2]/(2.0*L))*Math.sqrt(E/(4.0*G))))        ;}
	
double J(double x[])
{return   2.0/Math.sqrt(2)*x[0]*x[1]*(x[1]*x[1]/12.0+0.25*(x[0]+x[2])*(x[0]+x[2]));                    }
	
double R(double x[])
{return	Math.sqrt(0.25*(x[1]*x[1]+(x[0]+x[2])*(x[0]+x[2])));}

double M(double x[])
{return P*(L+x[1]/2.0);}

double to1(double x[])
{return P/(Math.sqrt(2)*x[0]*x[1]);}

double to2(double x[])
{return M(x)*R(x)/J(x);}

double toend(double x[])
{return Math.sqrt(to1(x)*to1(x)+(2.0*to1(x)*to2(x)*x[1]/(2.0*R(x)))+to2(x)*to2(x));}
	
	
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=990.000000015;
double s1=(1.10471*x[0]*x[0]*x[1])+(0.04811*x[2]*x[3]*(14.0+x[1]))+(mu*(H1(x)+H2(x)+H3(x)+H4(x)+H5(x)+H6(x)+H7(x)+H8(x)));
return s1;
}

double H1(double x[])
{return Math.pow(Math.max(0.0,         toend(x)-tomax                 ),2.0);}

double H2(double x[])
{return  Math.pow(Math.max(0.0,         sigma(x)-sigmax           ),2.0);}

double H3(double x[])
{return  Math.pow(Math.max(0.0,        x[0]-x[3]        ),2.0);}

double H4(double x[])
{return  Math.pow(Math.max(0.0,       -x[1]         ),2.0);}

double H5(double x[])
{return  Math.pow(Math.max(0.0,      0.125-x[0]         ),2.0);}

double H6(double x[])	
{return  Math.pow(Math.max(0.0,       delta(x)-deltamax         ),2.0);}

double H7(double x[])
{return  Math.pow(Math.max(0.0,       P-Pc(x)         ),2.0);}

double H8(double x[])
{return  Math.pow(Math.max(0.0,       -x[2]         ),2.0);}
}


class frac_01 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return   -((4.0*x[0]+2.0*x[1]+10)/(x[0]+2.0*x[1]+5))+mu*(G1(x)+G2(x));
}	
	
double G1(double x[])
{return Math.pow(Math.max(0.0,       x[0]+3.0*x[1]-30.0      ),2.0);                                       }	
	
double G2(double x[])
{return Math.pow(Math.max(0.0,       -x[0]+2.0*x[1]-5.0      ),2.0); }	
	
 
}


class frac_02 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return   Math.pow(((x[0]+x[1]+1.0)/(x[0]+x[1]+2.0)),1.5)*Math.pow(((x[0]+x[1]+3.0)/(x[0]+x[1]+4.0)),2.1)*Math.pow(((x[0]+x[1]+5.0)/(x[0]+x[1]+6.0)),1.2)*Math.pow(((x[0]+x[1]+7.0)/(x[0]+x[1]+8.0)),1.1)+mu*(H1(x));
}	
	
double H1(double x[])
{return Math.abs(x[0]-x[1]);                                       }	
 
}

class frac_03 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return    ((x[0]+x[1]+1.0)/(2.0*x[0]-x[1]+3.0)) ;
}	
	
                                       	
 
}



class frac_04 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return    -((8.0*x[0]+7.0*x[1]-2.33*Math.sqrt(9.0*x[0]*x[0]+4.0*x[1]*x[1]))/(20.0*x[0]+12.0*x[1]-2.33*Math.sqrt(3.0*x[0]*x[0]+2.0*x[0]*x[1]+4.0*x[1]*x[1])))+mu*(G1(x)+G2(x)) ;
}	

double G1(double x[])
{return Math.pow(Math.max(0.0,       2.0*x[0]+x[1]-18.0    ),2.0);}	
 
double G2(double x[])
{return Math.pow(Math.max(0.0,       x[0]+2.0*x[1]-16.0    ),2.0);}
                                      	
 
}


class frac_05 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return    -(((2.0*x[0]+x[1])/x[0])+(2.0/x[1]))+mu*(G1(x)+G2(x)+G3(x)) ;
}	

double G1(double x[])
{return Math.pow(Math.max(0.0,       2.0*x[0]+x[1]-6.0    ),2.0);}	
 
double G2(double x[])
{return Math.pow(Math.max(0.0,       3.0*x[0]+x[1]-8.0    ),2.0);}
   
double G3(double x[])
{return Math.pow(Math.max(0.0,           x[0]-x[1]-1.0    ),2.0);}
  
}

class frac_06 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=100.00007;

return    -(((-x[0]*x[0]+3.0*x[0]-x[1]*x[1]+3.0*x[1]+3.5)/(x[0]+1.0)) + (x[1]/(x[0]*x[0]-2.0*x[0]+x[1]*x[1]-8.0*x[1]+20.0)))+mu*(G1(x)+G2(x)+G3(x)) ;
}	

double G1(double x[])
{return Math.pow(Math.max(0.0,       2.0*x[0]+x[1]-6.0    ),2.0);}	
 
double G2(double x[])
{return Math.pow(Math.max(0.0,       3.0*x[0]+x[1]-8.0    ),2.0);}
   
double G3(double x[])
{return Math.pow(Math.max(0.0,           x[0]-x[1]-1.0    ),2.0);}
  
}


class frac_07 extends f_xj
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.20007;

return   -((((-x[0]*x[0]*Math.sqrt(x[1]))+2.0*x[0]*(1.0/x[1])-x[1]*x[1]+2.8*(1.0/x[0])*x[1]+7.5)/(x[0]*Math.pow(x[1],1.5)+1.0))+((x[1]+0.1)/(-x[0]*x[0]*(1.0/x[1])-(3.0/x[0])+2.0*x[0]*x[1]*x[1]+(9.0/x[1])+12.0)))+mu*(G1(x)+G2(x)+G3(x)) ;
}	

double G1(double x[])
{return Math.pow(Math.max(0.0,       (2.0/x[0])+x[0]*x[1]-4.0   ),2.0);}	
 
double G2(double x[])
{return Math.pow(Math.max(0.0,       x[0]+(3.0/x[0])*x[1]-5.0    ),2.0);}
   
double G3(double x[])
{return Math.pow(Math.max(0.0,       x[0]*x[0]-3.0*x[1]*x[1]*x[1]-2.0    ),2.0);}
  
}



// class frac_09 extends f_xj //---
// {
// 	
// public double func(double x[]) 
// {
// //çözümü istenen fonksiyon	
// double mu=110.20007;
// 
// return   -(((37.0*x[0]+73.0*x[1]+13.0)/(13.0*x[0]+13.0*x[1]+13))-((63.0*x[0]-18*x[1]+39)/(13.0*x[0]+26.0*x[1]+13.0)))+mu*(G1(x)) ;
// }	
// 
// double G1(double x[])
// {return return Math.pow(Math.max(0.0,       -x[0]*x[0]-x[1]*x[1]+3.0   ),2.0);}	
//  
// double G2(double x[])
// {return return Math.pow(Math.max(0.0,       -x[0]*x[0]-x[1]*x[1]+8.0*x[1]-3.0   ),2.0);} 
//  
// double G3(double x[])
// {return return Math.pow(Math.max(0.0,       2.0*x[0]+x[1]-6.0   ),2.0);} 
// 
// double G4(double x[])
// {return return Math.pow(Math.max(0.0,       3.0*x[0]+x[1]-8.0   ),2.0);} 
// 
// double G4(double x[])
// {return return Math.pow(Math.max(0.0,          x[0]-x[1]-1.0   ),2.0);}
//  
// }

class frac_09 extends f_xj //---
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.10007;

return  ((2.0*x[0]+x[1])/(x[0]+10.0))+(2.0/(x[1]+10))+mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)) ;
}	

double G1(double x[])
{return  Math.pow(Math.max(0.0,       -x[0]*x[0]-x[1]*x[1]+3.0   ),2.0);}	
 
double G2(double x[])
{return   Math.pow(Math.max(0.0,       -x[0]*x[0]-x[1]*x[1]+8.0*x[1]-3.0   ),2.0);} 
 
double G3(double x[])
{return   Math.pow(Math.max(0.0,       2.0*x[0]+x[1]-6.0   ),2.0);} 

double G4(double x[])
{return   Math.pow(Math.max(0.0,       3.0*x[0]+x[1]-8.0   ),2.0);} 

double G5(double x[])
{return   Math.pow(Math.max(0.0,          x[0]-x[1]-1.0   ),2.0);}
 
 
  
}


class fconstr1 extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=1000.0;
double ww=0.99;
//f[0]  min--> 0.3888915710232697     
//      x  --> 0.3888915710232697 2.4999977206914625  
//      max --> 0.9999999999898582
//      x   --> 4.107593163913976  0.9999999999898582
//f[1]  min -->1.0000005445914724 
//      x   --> 0.9999998321011433 3.7669252426105054E-7 
//      max -->8.999999999927631 
//      x   --> 0.567173791492188 4.104564123388647 

         

return  ww*((x[0]-0.3888915710232697)/(0.9999999999898582-0.3888915710232697))+((1-ww)*((((1+x[1])/x[0])-1.0000005445914724)/(8.999999999927631-1.0000005445914724)))+mu*(G1(x)+G2(x)) ;
}	

double G1(double x[])
{return  Math.pow(Math.max(0.0,       6.0-(x[1]+9.0*x[0])   ),2.0);}	
 
double G2(double x[])
{return   Math.pow(Math.max(0.0,      1.0+x[1]-9.0*x[0]  ),2.0);} 
 
  
  
}



class fconstr2 extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=5000.0;
 double ww=0.9999;
//f[0]  min--> 0.04269954053917328     
//      x  --> 0.04269954053917328 1.037934234857955  
//      max --> 1.2071059830615452
//      x   --> 1.2071059830615452  0.49893912978394145
//f[1]  min -->0.04187132187223135 
//      x   --> 1.0383476565351561 0.04187132187223135 
//      max -->1.2071067295252906 
//      x   --> 0.5000401889097337  1.2071067295252906 

         
//return -x[1]+mu*(G1(x)+G2(x));
return  ww*((x[0]-0.04269954053917328)/(1.2071059830615452-0.04269954053917328))+((1.0-ww)*((x[1]-0.04187132187223135)/(1.2071067295252906-0.04187132187223135)))+mu*(G1(x)+G2(x)) ;
}	

double G1(double x[])
{return  Math.pow(Math.max(0.0,       -x[0]*x[0]-x[1]*x[1]+1.0+0.1*Math.cos(16.0*Math.atan(x[1]/x[0]))   ),2.0);}	
 
double G2(double x[])
{return   Math.pow(Math.max(0.0,      (x[0]-0.5)*(x[0]-0.5)+(x[1]-0.5)*(x[1]-0.5)-0.5  ),2.0);} 
 
  
  
}



class fconstr3 extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=50000.0;
 double ww=0.42;
//f[0]  min--> -35.99966515994405     
//      x  --> 5.999980294663562 9.837640699106201E-5   
//      max --> 6.4998547263342195
//      x   --> 8.6590131773518E-4  6.499855476119311
//f[1]  min -->1.000000000000015 
//      x   --> 3.018749917823323E-14 5.5363794327159496E-17 
//      max -->8.499999999389313 
//      x   --> 4.3276933593990705  5.336153319689777 
      
double f1=-x[0]*x[0]+x[1];
double f2=0.5*x[0]+x[1]+1.0;

         
//return (0.5*x[0]+x[1]+1.0)+mu*(G1(x)+G2(x)+G3(x));
return  -ww*(f1)-((1.0-ww)*(f2))+mu*(G1(x)+G2(x)+G3(x)) ;
}	

double G1(double x[])
{return  Math.pow(Math.max(0.0,         (x[0]/6.0)+x[1]-6.5   ),2.0);}	
 
double G2(double x[])
{return   Math.pow(Math.max(0.0,   (x[0]/2.0)+x[1]-7.5     ),2.0);} 
 
double G3(double x[])
{return   Math.pow(Math.max(0.0,   (x[0]*5.0)+x[1]-30.0     ),2.0);}   
  
}

class threebartruss_multi extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double F=10;
double E=2e5;
double L=200;
double sigma=10;
 double ww=1.00;
//f[0]  min--> -35.99966515994405     
//      x  --> 5.999980294663562 9.837640699106201E-5   
//      max --> 6.4998547263342195
//      x   --> 8.6590131773518E-4  6.499855476119311
//f[1]  min -->1.000000000000015 
//      x   --> 3.018749917823323E-14 5.5363794327159496E-17 
//      max -->8.499999999389313 
//      x   --> 4.3276933593990705  5.336153319689777 
      
double f1=L*(2*x[0]+Math.sqrt(2*x[1])+Math.sqrt(x[2])+x[3]);
double f2=((F*L)/E)*((2/x[1])+(2*Math.sqrt(2)/x[1])-(2*Math.sqrt(2)/x[2])+(2/x[3]));

         
//return (0.5*x[0]+x[1]+1.0)+mu*(G1(x)+G2(x)+G3(x));
return  ww*(f1)+((1.0-ww)*(f2)) ;
}	
}

class weldedbeam_multi extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=0.1;
 double ww=0.1;
 //double ww=0.00000001;
//f[0]  min--> -35.99966515994405     
//      x  --> 5.999980294663562 9.837640699106201E-5   
//      max --> 6.4998547263342195
//      x   --> 8.6590131773518E-4  6.499855476119311
//f[1]  min -->1.000000000000015 
//      x   --> 3.018749917823323E-14 5.5363794327159496E-17 
//      max -->8.499999999389313 
//      x   --> 4.3276933593990705  5.336153319689777 
      
double f1=1.10471*x[0]*x[0]*x[1]+0.04811*x[2]*x[3]*(14.0+x[2]);
double f2=2.1952/(x[2]*x[2]*x[2]*x[3]);


         
//return (0.5*x[0]+x[1]+1.0)+mu*(G1(x)+G2(x)+G3(x));
return  ww*(f1)+((1.0-ww)*(f2))+mu*(G1(x)+G2(x)+G3(x)+G4(x)) ;
}
double G1(double x[])
{
	double to11=(6000.0*(14.0+0.5*x[1])*Math.sqrt(0.25*(x[1]*x[1]+(x[0]+x[2])*(x[0]+x[2]))))/(2*(0.707*x[0]*x[1]*(x[1]*x[1]/12.0)+0.25*(x[1]*x[1]+(x[0]+x[2])*(x[0]+x[2]))));
	double to1=6000.0/(Math.sqrt(2)*x[0]*x[1]);
	double to=Math.sqrt(to1*to1+to11*to11+(x[1]*to1*to11)/Math.sqrt(0.25*(x[1]*x[1]+(x[0]+x[2])*(x[0]+x[2]))));
	return  Math.pow(Math.max(0.0,        to-13600.0  ),2.0);
}	
 
double G2(double x[])
{
	double sigma=504000/(x[2]*x[2]*x[3]);
	return   Math.pow(Math.max(0.0,   sigma-30000.0     ),2.0);
} 
 
double G3(double x[])
{return   Math.pow(Math.max(0.0,   x[0]-x[3]    ),2.0);} 

double G4(double x[])
{
	double pcx=64764.022*(1.0-0.0282346*x[2])*x[2]*x[3]*x[3]*x[3];
	return   Math.pow(Math.max(0.0,   6000.0-pcx    ),2.0);
}
} 
class diskbrake_multi extends f_xj  
{
	
public double func(double x[]) 
{
//çözümü istenen fonksiyon	
double mu=10.0;
double ww=0.00;
//f[0]  min--> -35.99966515994405     
//      x  --> 5.999980294663562 9.837640699106201E-5   
//      max --> 6.4998547263342195
//      x   --> 8.6590131773518E-4  6.499855476119311
//f[1]  min -->1.000000000000015 
//      x   --> 3.018749917823323E-14 5.5363794327159496E-17 
//      max -->8.499999999389313 
//      x   --> 4.3276933593990705  5.336153319689777 
      
double f1=4.9e-5*(x[1]*x[1]-x[0]*x[0])*(x[3]-1.0);
double f2=9.82e6*(x[1]*x[1]-x[0]*x[0])/(x[2]*x[3]*(x[1]*x[1]*x[1]-x[0]*x[0]*x[0]));

         
//return (0.5*x[0]+x[1]+1.0)+mu*(G1(x)+G2(x)+G3(x));
return  ww*(f1)+((1.0-ww)*(f2))+mu*(G1(x)+G2(x)+G3(x)+G4(x)+G5(x)) ;
}
double G1(double x[])
{return  Math.pow(Math.max(0.0,   20.0+x[0]-x[1]        ),2.0);}	
 
double G2(double x[])
{return   Math.pow(Math.max(0.0,  2.5*(x[3]+1.0)-30.0  ),2.0);} 
 
double G3(double x[])
{return   Math.pow(Math.max(0.0,   (x[2]/(3.14*(x[1]*x[1]-x[0]*x[0])*(x[1]*x[1]-x[0]*x[0])))-0.4  ),2.0);}   	

double G4(double x[])
{return  Math.pow(Math.max(0.0,        (2.22e-3*x[2]*(x[1]*x[1]*x[1]-x[0]*x[0]*x[0]))/((x[1]*x[1]-x[0]*x[0])*(x[1]*x[1]-x[0]*x[0]))-1 ),2.0);}	
 
double G5(double x[])
{return   Math.pow(Math.max(0.0,  900.0-(2.66e-2*x[2]*x[3]*(x[1]*x[1]*x[1]-x[0]*x[0]*x[0]))/((x[1]*x[1]-x[0]*x[0]))  ),2.0);} 
 
  	 	  	
}


class muzo extends f_xj // Rastrigin plus Ackley Function          
{
public double func(double x[]) 
{
 
// double[] V_1={-3.013500E+00,-2.988202E+00,-2.963134E+00,-2.937211E+00,-2.911229E+00,-2.885934E+00,-2.861603E+00,-2.835963E+00,-2.810766E+00,-2.785139E+00,-2.759495E+00,-2.734386E+00,-2.709140E+00,-2.683637E+00,-2.658489E+00,-2.632618E+00,-2.607396E+00,-2.581915E+00,-2.556926E+00,-2.531982E+00,-2.506359E+00,-2.480233E+00,-2.455509E+00,-2.430305E+00,-2.404343E+00,-2.379418E+00,-2.353690E+00,-2.328300E+00,-2.303312E+00,-2.278181E+00,-2.252111E+00,-2.227057E+00,-2.201410E+00,-2.175841E+00,-2.150763E+00,-2.125752E+00,-2.100199E+00,-2.074925E+00,-2.049066E+00,-2.023834E+00,-1.999198E+00,-1.973410E+00,-1.948333E+00,-1.923028E+00,-1.896797E+00,-1.871978E+00,-1.847000E+00,-1.820620E+00,-1.796179E+00,-1.770976E+00,-1.745499E+00,-1.720904E+00,-1.694640E+00,-1.668698E+00,-1.643591E+00,-1.617638E+00,-1.591801E+00,-1.567788E+00,-1.542263E+00,-1.517094E+00,-1.491944E+00,-1.466032E+00,-1.441077E+00,-1.415100E+00,-1.389956E+00,-1.364830E+00,-1.339323E+00,-1.313771E+00,-1.288738E+00,-1.263122E+00,-1.237698E+00,-1.212741E+00,-1.186680E+00,-1.161807E+00,-1.136024E+00,-1.110582E+00,-1.085286E+00,-1.059504E+00,-1.034202E+00,-1.008816E+00,-9.835417E-01,-9.587035E-01,-9.336078E-01,-9.089324E-01,-8.820977E-01,-8.568176E-01,-8.314713E-01,-8.068262E-01,-7.806045E-01,-7.558147E-01,-7.304323E-01,-7.046033E-01,-6.790178E-01,-6.533033E-01,-6.290466E-01,-6.028415E-01,-5.778965E-01,-5.527887E-01,-5.275939E-01,-5.022327E-01,-4.769648E-01,-4.506831E-01,-4.253254E-01,-3.999785E-01,-3.749537E-01,-3.497598E-01,-3.250624E-01,-2.982886E-01,-2.729308E-01,-2.479166E-01,-2.228869E-01,-1.975325E-01,-1.719975E-01,-1.458941E-01,-1.212791E-01,-9.742565E-02,+1.111038E-01,+1.367147E-01,+1.632435E-01,+1.885170E-01,+2.134580E-01,+2.387390E-01,+2.644154E-01,+2.893595E-01,+3.150591E-01,+3.404985E-01,+3.656774E-01,+3.905315E-01,+4.159831E-01,+4.411799E-01,+4.673044E-01,+4.927268E-01,+5.185922E-01,+5.427643E-01,+5.682280E-01,+5.936539E-01,+6.201126E-01,+6.452207E-01,+6.706535E-01,+6.955945E-01,+7.211145E-01,+7.467276E-01,+7.717416E-01,+7.972661E-01,+8.223515E-01,+8.475474E-01,+8.733628E-01,+8.985012E-01,+9.234832E-01,+9.491813E-01,+9.745657E-01,+9.996139E-01,+1.024651E+00,+1.051129E+00,+1.076389E+00,+1.101188E+00,+1.127394E+00,+1.151956E+00,+1.176865E+00,+1.202373E+00,+1.228797E+00,+1.253471E+00,+1.278769E+00,+1.304429E+00,+1.329954E+00,+1.354468E+00,+1.380243E+00,+1.405898E+00,+1.431552E+00,+1.456140E+00,+1.482258E+00,+1.508157E+00,+1.532722E+00,+1.558519E+00,+1.583428E+00,+1.608085E+00,+1.634498E+00,+1.659261E+00,+1.684357E+00,+1.710109E+00,+1.734905E+00,+1.760632E+00,+1.786336E+00,+1.811371E+00,+1.836957E+00,+1.861885E+00,+1.887711E+00,+1.912145E+00,+1.939030E+00,+1.963883E+00,+1.989815E+00,+2.014312E+00,+2.041030E+00,+2.065123E+00,+2.091009E+00,+2.115102E+00,+2.141541E+00,+2.166945E+00,+2.192136E+00,+2.217223E+00,+2.243428E+00,+2.268673E+00,+2.293880E+00,+2.319594E+00,+2.344717E+00,+2.370790E+00,+2.395036E+00,+2.421158E+00,+2.446050E+00,+2.471542E+00,+2.497083E+00,+2.521572E+00,+2.547683E+00,+2.572958E+00,+2.597009E+00,+2.622990E+00,+2.649802E+00,+2.674326E+00,+2.700066E+00,+2.724342E+00,+2.750477E+00,+2.775574E+00,+2.801507E+00,+2.826569E+00,+2.851939E+00,+2.877118E+00,+2.902578E+00,+2.927814E+00,+2.953354E+00,+2.978426E+00,+3.004220E+00}; 
// double[] I_1={-1.619359E-05,-1.590154E-05,-1.574869E-05,-1.561282E-05,-1.536612E-05,-1.517332E-05,-1.496365E-05,-1.475702E-05,-1.468902E-05,-1.463971E-05,-1.451944E-05,-1.434218E-05,-1.414879E-05,-1.397870E-05,-1.379126E-05,-1.367068E-05,-1.346617E-05,-1.349306E-05,-1.330013E-05,-1.314913E-05,-1.300309E-05,-1.294544E-05,-1.271423E-05,-1.256523E-05,-1.249342E-05,-1.236171E-05,-1.231191E-05,-1.213741E-05,-1.209796E-05,-1.194558E-05,-1.167763E-05,-1.160240E-05,-1.159638E-05,-1.136092E-05,-1.119216E-05,-1.111299E-05,-1.102942E-05,-1.079211E-05,-1.073998E-05,-1.063049E-05,-1.045936E-05,-1.030956E-05,-1.014329E-05,-1.003763E-05,-9.923904E-06,-9.864546E-06,-9.721170E-06,-9.598626E-06,-9.431755E-06,-9.325462E-06,-9.099183E-06,-9.025924E-06,-8.857488E-06,-8.716310E-06,-8.549148E-06,-8.449370E-06,-8.300171E-06,-8.087643E-06,-7.920605E-06,-7.903353E-06,-7.715634E-06,-7.578486E-06,-7.381171E-06,-7.315147E-06,-7.150230E-06,-6.998486E-06,-6.884384E-06,-6.777226E-06,-6.586913E-06,-6.523684E-06,-6.388015E-06,-6.262434E-06,-6.163631E-06,-6.063616E-06,-5.923564E-06,-5.854580E-06,-5.644428E-06,-5.491287E-06,-5.379261E-06,-5.291031E-06,-5.126662E-06,-4.945502E-06,-4.791330E-06,-4.693592E-06,-4.605759E-06,-4.442740E-06,-4.316469E-06,-4.229018E-06,-4.113605E-06,-3.936268E-06,-3.791000E-06,-3.660567E-06,-3.544907E-06,-3.345302E-06,-3.249670E-06,-3.150717E-06,-3.048179E-06,-2.946830E-06,-2.812597E-06,-2.733669E-06,-2.596405E-06,-2.445433E-06,-2.338330E-06,-2.236126E-06,-2.067292E-06,-1.910833E-06,-1.815578E-06,-1.701688E-06,-1.556756E-06,-1.424603E-06,-1.351085E-06,-1.221940E-06,-1.062264E-06,-9.066040E-07,-7.733005E-07,-6.323909E-07,+1.429335E-06,+2.013769E-06,+2.796494E-06,+3.757287E-06,+4.994158E-06,+6.462847E-06,+8.054579E-06,+9.959630E-06,+1.208227E-05,+1.454667E-05,+1.706022E-05,+1.979599E-05,+2.275687E-05,+2.600284E-05,+2.937962E-05,+3.287807E-05,+3.655003E-05,+4.032738E-05,+4.426748E-05,+4.817922E-05,+5.243013E-05,+5.669309E-05,+6.114500E-05,+6.569402E-05,+7.035016E-05,+7.511862E-05,+7.991806E-05,+8.485822E-05,+9.004369E-05,+9.509322E-05,+1.004159E-04,+1.061209E-04,+1.117297E-04,+1.172400E-04,+1.228488E-04,+1.285385E-04,+1.343791E-04,+1.405022E-04,+1.465146E-04,+1.526874E-04,+1.589446E-04,+1.652152E-04,+1.716585E-04,+1.783249E-04,+1.850870E-04,+1.917744E-04,+1.984882E-04,+2.054864E-04,+2.126933E-04,+2.196528E-04,+2.272601E-04,+2.348586E-04,+2.422845E-04,+2.498468E-04,+2.576643E-04,+2.662881E-04,+2.746704E-04,+2.833803E-04,+2.912279E-04,+3.004223E-04,+3.096872E-04,+3.191025E-04,+3.271497E-04,+3.380389E-04,+3.469102E-04,+3.577153E-04,+3.664095E-04,+3.767420E-04,+3.906724E-04,+4.030817E-04,+4.238049E-04,+4.368431E-04,+4.497614E-04,+4.698079E-04,+4.830401E-04,+5.088538E-04,+5.279918E-04,+5.396999E-04,+5.596962E-04,+5.742605E-04,+5.976629E-04,+6.200543E-04,+6.417045E-04,+6.672438E-04,+6.906921E-04,+7.247625E-04,+7.419763E-04,+7.709179E-04,+8.074756E-04,+8.332052E-04,+8.665686E-04,+9.005624E-04,+9.351113E-04,+9.648481E-04,+9.983662E-04,+1.014226E-03,+1.041738E-03,+1.054917E-03,+1.093688E-03,+1.115386E-03,+1.164241E-03,+1.216252E-03,+1.241885E-03,+1.285892E-03,+1.359932E-03,+1.431119E-03,+1.470528E-03,+1.526352E-03,+1.556017E-03,+1.595544E-03,+1.637721E-03,+1.686287E-03,+1.775307E-03,+1.829746E-03,+1.868379E-03};
// 
// int I1len=I_1.length;
// for(int i=0;i<I1len;i++)
// {I_1[i]=I_1[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_2={-3.013504E+00,-2.988596E+00,-2.962367E+00,-2.937220E+00,-2.912151E+00,-2.886332E+00,-2.861019E+00,-2.835828E+00,-2.811024E+00,-2.784547E+00,-2.759754E+00,-2.734044E+00,-2.708658E+00,-2.683394E+00,-2.658561E+00,-2.632583E+00,-2.607510E+00,-2.581950E+00,-2.557124E+00,-2.531435E+00,-2.506159E+00,-2.480434E+00,-2.455175E+00,-2.429351E+00,-2.404408E+00,-2.379886E+00,-2.353909E+00,-2.328806E+00,-2.303772E+00,-2.277750E+00,-2.252566E+00,-2.227262E+00,-2.201419E+00,-2.175787E+00,-2.150069E+00,-2.125752E+00,-2.100267E+00,-2.074730E+00,-2.049303E+00,-2.023582E+00,-1.998154E+00,-1.973159E+00,-1.947481E+00,-1.922855E+00,-1.896945E+00,-1.871844E+00,-1.846716E+00,-1.821012E+00,-1.795964E+00,-1.770785E+00,-1.744902E+00,-1.720070E+00,-1.693995E+00,-1.669604E+00,-1.644453E+00,-1.618410E+00,-1.593168E+00,-1.568382E+00,-1.542451E+00,-1.517470E+00,-1.492571E+00,-1.466002E+00,-1.440772E+00,-1.416230E+00,-1.389845E+00,-1.364431E+00,-1.339787E+00,-1.313574E+00,-1.288532E+00,-1.263073E+00,-1.238165E+00,-1.212808E+00,-1.186740E+00,-1.160798E+00,-1.136259E+00,-1.110567E+00,-1.085217E+00,-1.059950E+00,-1.034619E+00,-1.009874E+00,-9.840991E-01,-9.589280E-01,-9.331217E-01,-9.081288E-01,-8.825029E-01,-8.564121E-01,-8.319439E-01,-8.063637E-01,-7.810110E-01,-7.559968E-01,-7.305551E-01,-7.042696E-01,-6.796623E-01,-6.544446E-01,-6.288501E-01,-6.032423E-01,-5.778208E-01,-5.522845E-01,-5.275211E-01,-5.017558E-01,-4.762878E-01,-4.509303E-01,-4.256648E-01,-4.003767E-01,-3.747844E-01,-3.498338E-01,-3.247178E-01,-2.982907E-01,-2.730958E-01,-2.481601E-01,-2.230417E-01,-1.976159E-01,-1.719210E-01,-1.467214E-01,-1.207827E-01,-9.575805E-02,+1.121949E-01,+1.361308E-01,+1.631612E-01,+1.878421E-01,+2.132898E-01,+2.386444E-01,+2.641670E-01,+2.899473E-01,+3.150591E-01,+3.406745E-01,+3.654517E-01,+3.906175E-01,+4.161639E-01,+4.413379E-01,+4.668527E-01,+4.926567E-01,+5.177371E-01,+5.427570E-01,+5.682846E-01,+5.938126E-01,+6.196746E-01,+6.450582E-01,+6.701125E-01,+6.957707E-01,+7.206829E-01,+7.464674E-01,+7.717364E-01,+7.975271E-01,+8.221902E-01,+8.474868E-01,+8.735414E-01,+8.983512E-01,+9.232666E-01,+9.486553E-01,+9.745395E-01,+1.000225E+00,+1.025082E+00,+1.051100E+00,+1.075762E+00,+1.101156E+00,+1.126348E+00,+1.152078E+00,+1.177040E+00,+1.202898E+00,+1.228864E+00,+1.253155E+00,+1.278962E+00,+1.304218E+00,+1.329861E+00,+1.355070E+00,+1.380085E+00,+1.405629E+00,+1.431032E+00,+1.455649E+00,+1.482027E+00,+1.507803E+00,+1.532723E+00,+1.558248E+00,+1.582427E+00,+1.608368E+00,+1.634332E+00,+1.660201E+00,+1.684986E+00,+1.710368E+00,+1.735404E+00,+1.761061E+00,+1.786133E+00,+1.812290E+00,+1.837207E+00,+1.862577E+00,+1.887615E+00,+1.913666E+00,+1.939531E+00,+1.963760E+00,+1.989842E+00,+2.014713E+00,+2.039806E+00,+2.065248E+00,+2.090926E+00,+2.116091E+00,+2.140888E+00,+2.166945E+00,+2.191817E+00,+2.218343E+00,+2.243155E+00,+2.268711E+00,+2.293865E+00,+2.318629E+00,+2.343768E+00,+2.369825E+00,+2.394317E+00,+2.421058E+00,+2.445917E+00,+2.471208E+00,+2.496366E+00,+2.522891E+00,+2.547566E+00,+2.573309E+00,+2.597830E+00,+2.623359E+00,+2.649051E+00,+2.674320E+00,+2.699511E+00,+2.724545E+00,+2.750393E+00,+2.775390E+00,+2.800866E+00,+2.826588E+00,+2.852549E+00,+2.876900E+00,+2.902431E+00,+2.927230E+00,+2.954355E+00,+2.978903E+00,+3.004157E+00};
// double[] I_2={-1.616352E-05,-1.584848E-05,-1.574232E-05,-1.542639E-05,-1.526602E-05,-1.508313E-05,-1.487815E-05,-1.476971E-05,-1.454516E-05,-1.447077E-05,-1.433888E-05,-1.419823E-05,-1.395411E-05,-1.386615E-05,-1.362146E-05,-1.348406E-05,-1.332325E-05,-1.317918E-05,-1.317266E-05,-1.295247E-05,-1.277978E-05,-1.279756E-05,-1.264499E-05,-1.247040E-05,-1.229231E-05,-1.219347E-05,-1.199185E-05,-1.186424E-05,-1.194592E-05,-1.172075E-05,-1.163444E-05,-1.151333E-05,-1.132448E-05,-1.114086E-05,-1.100102E-05,-1.086399E-05,-1.075532E-05,-1.062098E-05,-1.053804E-05,-1.041529E-05,-1.032235E-05,-1.013105E-05,-1.002268E-05,-9.861357E-06,-9.756490E-06,-9.548859E-06,-9.482282E-06,-9.291867E-06,-9.172753E-06,-9.057088E-06,-8.927713E-06,-8.795744E-06,-8.692696E-06,-8.570237E-06,-8.422242E-06,-8.319524E-06,-8.150273E-06,-8.038925E-06,-7.924798E-06,-7.798753E-06,-7.708419E-06,-7.627590E-06,-7.503059E-06,-7.351401E-06,-7.264280E-06,-7.102016E-06,-6.977163E-06,-6.786675E-06,-6.685608E-06,-6.585003E-06,-6.434054E-06,-6.282244E-06,-6.189615E-06,-6.068622E-06,-5.906635E-06,-5.746647E-06,-5.613474E-06,-5.497649E-06,-5.348299E-06,-5.166271E-06,-5.107631E-06,-5.000756E-06,-4.876514E-06,-4.741588E-06,-4.611119E-06,-4.503441E-06,-4.343266E-06,-4.199606E-06,-4.180991E-06,-4.044621E-06,-3.951153E-06,-3.737309E-06,-3.586370E-06,-3.496642E-06,-3.365587E-06,-3.240217E-06,-3.135890E-06,-2.998944E-06,-2.809526E-06,-2.626898E-06,-2.479908E-06,-2.357792E-06,-2.183961E-06,-2.098349E-06,-1.999921E-06,-1.895726E-06,-1.770017E-06,-1.635761E-06,-1.555870E-06,-1.424856E-06,-1.266850E-06,-1.147156E-06,-1.014884E-06,-8.859586E-07,-7.010487E-07,-5.898128E-07,+1.357517E-06,+1.972646E-06,+2.778328E-06,+3.737450E-06,+4.960717E-06,+6.451532E-06,+8.164695E-06,+1.010328E-05,+1.224952E-05,+1.465593E-05,+1.721735E-05,+2.004803E-05,+2.295867E-05,+2.620501E-05,+2.973945E-05,+3.334358E-05,+3.696937E-05,+4.075432E-05,+4.481049E-05,+4.882335E-05,+5.310151E-05,+5.730032E-05,+6.164175E-05,+6.624085E-05,+7.092137E-05,+7.581285E-05,+8.052184E-05,+8.546828E-05,+9.040006E-05,+9.547907E-05,+1.005886E-04,+1.064008E-04,+1.118905E-04,+1.173870E-04,+1.228747E-04,+1.286003E-04,+1.343059E-04,+1.402816E-04,+1.462251E-04,+1.522942E-04,+1.581082E-04,+1.642823E-04,+1.704877E-04,+1.770606E-04,+1.835872E-04,+1.900085E-04,+1.966572E-04,+2.034275E-04,+2.104314E-04,+2.173994E-04,+2.245631E-04,+2.317065E-04,+2.389280E-04,+2.463451E-04,+2.542102E-04,+2.617884E-04,+2.699428E-04,+2.777610E-04,+2.862398E-04,+2.947146E-04,+3.037815E-04,+3.130133E-04,+3.226666E-04,+3.331939E-04,+3.437067E-04,+3.555530E-04,+3.655054E-04,+3.767514E-04,+3.903804E-04,+4.027118E-04,+4.166199E-04,+4.325832E-04,+4.471380E-04,+4.623578E-04,+4.793222E-04,+4.939647E-04,+5.129421E-04,+5.314602E-04,+5.517844E-04,+5.726658E-04,+5.900600E-04,+6.114708E-04,+6.300182E-04,+6.502089E-04,+6.863471E-04,+7.093204E-04,+7.361697E-04,+7.552083E-04,+7.785034E-04,+8.094859E-04,+8.366263E-04,+8.707435E-04,+9.072158E-04,+9.276123E-04,+9.524317E-04,+9.913519E-04,+1.035009E-03,+1.058011E-03,+1.106605E-03,+1.146599E-03,+1.176545E-03,+1.221466E-03,+1.265550E-03,+1.312351E-03,+1.333740E-03,+1.412803E-03,+1.440637E-03,+1.491412E-03,+1.533755E-03,+1.581845E-03,+1.677771E-03,+1.739463E-03,+1.796370E-03,+1.864901E-03,+1.907429E-03};
// 
// 
// int I2len=I_2.length;
// for(int i=0;i<I2len;i++)
// {I_2[i]=I_2[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_3={-3.013978E+00,-2.983824E+00,-2.954315E+00,-2.924619E+00,-2.895410E+00,-2.865831E+00,-2.836245E+00,-2.806643E+00,-2.776827E+00,-2.747288E+00,-2.717231E+00,-2.687536E+00,-2.657691E+00,-2.628276E+00,-2.599533E+00,-2.569487E+00,-2.539492E+00,-2.509879E+00,-2.480567E+00,-2.451021E+00,-2.421359E+00,-2.391597E+00,-2.361881E+00,-2.332658E+00,-2.302926E+00,-2.273336E+00,-2.243701E+00,-2.213913E+00,-2.183603E+00,-2.155394E+00,-2.125473E+00,-2.095723E+00,-2.065527E+00,-2.036803E+00,-2.006582E+00,-1.977036E+00,-1.947411E+00,-1.918042E+00,-1.887840E+00,-1.858961E+00,-1.828139E+00,-1.799575E+00,-1.769939E+00,-1.740005E+00,-1.710178E+00,-1.681248E+00,-1.651204E+00,-1.621529E+00,-1.592396E+00,-1.562199E+00,-1.532086E+00,-1.502922E+00,-1.473152E+00,-1.444359E+00,-1.414397E+00,-1.384478E+00,-1.355377E+00,-1.325449E+00,-1.295763E+00,-1.266067E+00,-1.236422E+00,-1.206733E+00,-1.177254E+00,-1.147527E+00,-1.118180E+00,-1.088843E+00,-1.058643E+00,-1.029684E+00,-9.998775E-01,-9.701055E-01,-9.400890E-01,-9.105417E-01,-8.797681E-01,-8.511627E-01,-8.216560E-01,-7.920164E-01,-7.627586E-01,-7.327288E-01,-7.037359E-01,-6.735821E-01,-6.440226E-01,-6.141025E-01,-5.849591E-01,-5.551205E-01,-5.257694E-01,-4.961435E-01,-4.655370E-01,-4.373252E-01,-4.071632E-01,-3.776180E-01,-3.478389E-01,-3.182182E-01,-2.886126E-01,-2.589207E-01,-2.293068E-01,-1.998574E-01,-1.702458E-01,-1.402190E-01,-1.103561E-01,+9.934680E-02,+1.301286E-01,+1.594823E-01,+1.897734E-01,+2.192985E-01,+2.488342E-01,+2.785083E-01,+3.077834E-01,+3.375630E-01,+3.681946E-01,+3.973899E-01,+4.272612E-01,+4.569575E-01,+4.859801E-01,+5.155857E-01,+5.451108E-01,+5.744610E-01,+6.042514E-01,+6.336838E-01,+6.637167E-01,+6.935972E-01,+7.233173E-01,+7.523593E-01,+7.817671E-01,+8.112796E-01,+8.413975E-01,+8.699735E-01,+9.001863E-01,+9.301564E-01,+9.602884E-01,+9.893541E-01,+1.019431E+00,+1.049196E+00,+1.078558E+00,+1.108323E+00,+1.137434E+00,+1.167450E+00,+1.197107E+00,+1.226203E+00,+1.256299E+00,+1.286363E+00,+1.315699E+00,+1.345079E+00,+1.374265E+00,+1.404526E+00,+1.433814E+00,+1.462852E+00,+1.492424E+00,+1.522514E+00,+1.552075E+00,+1.581530E+00,+1.611904E+00,+1.641255E+00,+1.671045E+00,+1.699944E+00,+1.730364E+00,+1.759774E+00,+1.789482E+00,+1.818127E+00,+1.848196E+00,+1.877592E+00,+1.907609E+00,+1.937164E+00,+1.967373E+00,+1.996502E+00,+2.025974E+00,+2.055934E+00,+2.085658E+00,+2.115270E+00,+2.145217E+00,+2.174398E+00,+2.203677E+00,+2.233236E+00,+2.262576E+00,+2.292104E+00,+2.322711E+00,+2.351938E+00,+2.381455E+00,+2.411199E+00,+2.440911E+00,+2.470557E+00,+2.499719E+00,+2.529031E+00,+2.559160E+00,+2.589092E+00,+2.618319E+00,+2.647949E+00,+2.677681E+00,+2.707889E+00,+2.736705E+00,+2.767002E+00,+2.796402E+00,+2.825947E+00,+2.853197E+00,+2.853158E+00,+2.854410E+00,+2.853345E+00,+2.851047E+00,+2.851426E+00};
// double[] I_3={-2.181280E-06,-2.110557E-06,-2.070740E-06,-2.027489E-06,-1.975956E-06,-1.942753E-06,-1.974619E-06,-1.916461E-06,-1.875186E-06,-1.836085E-06,-1.843338E-06,-1.805134E-06,-1.742668E-06,-1.727410E-06,-1.711876E-06,-1.695557E-06,-1.669722E-06,-1.677688E-06,-1.660023E-06,-1.609017E-06,-1.578747E-06,-1.563242E-06,-1.545850E-06,-1.485177E-06,-1.471360E-06,-1.472405E-06,-1.444000E-06,-1.422777E-06,-1.447992E-06,-1.424041E-06,-1.395267E-06,-1.344903E-06,-1.325968E-06,-1.312782E-06,-1.241184E-06,-1.236555E-06,-1.226453E-06,-1.225478E-06,-1.195707E-06,-1.172288E-06,-1.188841E-06,-1.160707E-06,-1.109058E-06,-1.102434E-06,-1.082604E-06,-1.014538E-06,-1.009324E-06,-1.008670E-06,-9.945592E-07,-9.679080E-07,-9.502240E-07,-9.695125E-07,-9.345671E-07,-8.902542E-07,-8.789517E-07,-8.717427E-07,-7.978030E-07,-7.924335E-07,-7.854737E-07,-7.850567E-07,-7.543398E-07,-7.348340E-07,-7.694737E-07,-7.339602E-07,-6.903857E-07,-6.672855E-07,-6.534877E-07,-5.946708E-07,-5.770545E-07,-5.769750E-07,-5.723115E-07,-5.509045E-07,-5.316812E-07,-5.521630E-07,-5.159750E-07,-4.805901E-07,-4.592067E-07,-4.453179E-07,-4.220998E-07,-3.689061E-07,-3.742271E-07,-3.664012E-07,-3.463212E-07,-3.278589E-07,-3.449241E-07,-3.203078E-07,-2.776455E-07,-2.602431E-07,-2.433302E-07,-1.846383E-07,-1.902688E-07,-1.850503E-07,-1.713742E-07,-1.505227E-07,-1.740029E-07,-1.553983E-07,-1.149013E-07,-8.283016E-08,-6.919149E-08,+6.414584E-08,+1.286271E-07,+8.011651E-08,+1.253338E-07,+1.968598E-07,+2.719825E-07,+3.873593E-07,+6.016789E-07,+9.311552E-07,+1.453927E-06,+2.323405E-06,+3.840387E-06,+6.131056E-06,+9.668635E-06,+1.495499E-05,+2.239042E-05,+3.208081E-05,+4.505277E-05,+6.027253E-05,+7.868759E-05,+1.003781E-04,+1.251478E-04,+1.523152E-04,+1.813488E-04,+2.114907E-04,+2.456169E-04,+2.796802E-04,+3.173758E-04,+3.559761E-04,+3.973244E-04,+4.379567E-04,+4.810744E-04,+5.247899E-04,+5.710895E-04,+6.177103E-04,+6.667703E-04,+7.158569E-04,+7.673716E-04,+8.202465E-04,+8.758184E-04,+9.341750E-04,+9.950612E-04,+1.056085E-03,+1.118139E-03,+1.185647E-03,+1.256766E-03,+1.331098E-03,+1.404757E-03,+1.491192E-03,+1.576557E-03,+1.664194E-03,+1.773144E-03,+1.867474E-03,+1.971588E-03,+2.123220E-03,+2.248415E-03,+2.426040E-03,+2.657280E-03,+2.871410E-03,+3.193636E-03,+3.496472E-03,+3.800036E-03,+4.371819E-03,+4.875600E-03,+5.338881E-03,+5.896288E-03,+6.616284E-03,+7.246499E-03,+7.892600E-03,+8.476139E-03,+9.207429E-03,+9.800182E-03,+1.045672E-02,+1.109058E-02,+1.187467E-02,+1.254698E-02,+1.336296E-02,+1.404141E-02,+1.474280E-02,+1.553243E-02,+1.629924E-02,+1.721868E-02,+1.816174E-02,+1.893261E-02,+1.982777E-02,+2.072950E-02,+2.179278E-02,+2.285789E-02,+2.389078E-02,+2.487724E-02,+2.589659E-02,+2.710649E-02,+2.842254E-02,+2.962303E-02,+2.962388E-02,+2.962305E-02,+2.962450E-02,+2.962532E-02,+2.962492E-02};
// 
// int I3len=I_3.length;
// for(int i=0;i<I3len;i++)
// {I_3[i]=I_3[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_4={-3.013730E+00,-2.983494E+00,-2.954224E+00,-2.925218E+00,-2.895071E+00,-2.865542E+00,-2.835494E+00,-2.806891E+00,-2.776910E+00,-2.747205E+00,-2.717221E+00,-2.687720E+00,-2.658513E+00,-2.628513E+00,-2.599471E+00,-2.569869E+00,-2.540473E+00,-2.509845E+00,-2.480751E+00,-2.451138E+00,-2.421176E+00,-2.391769E+00,-2.362203E+00,-2.332085E+00,-2.302781E+00,-2.273700E+00,-2.243656E+00,-2.213475E+00,-2.184545E+00,-2.155140E+00,-2.125390E+00,-2.095569E+00,-2.065513E+00,-2.036095E+00,-2.006025E+00,-1.976729E+00,-1.947731E+00,-1.917144E+00,-1.887923E+00,-1.858619E+00,-1.828674E+00,-1.799766E+00,-1.769832E+00,-1.739635E+00,-1.710336E+00,-1.680755E+00,-1.651120E+00,-1.621747E+00,-1.591949E+00,-1.562262E+00,-1.532461E+00,-1.502967E+00,-1.472621E+00,-1.444471E+00,-1.414221E+00,-1.384506E+00,-1.354913E+00,-1.325402E+00,-1.295829E+00,-1.266018E+00,-1.235729E+00,-1.206643E+00,-1.177580E+00,-1.147777E+00,-1.118014E+00,-1.088637E+00,-1.058775E+00,-1.028746E+00,-9.999542E-01,-9.696180E-01,-9.401977E-01,-9.103159E-01,-8.811145E-01,-8.515078E-01,-8.216842E-01,-7.915994E-01,-7.636505E-01,-7.332979E-01,-7.032828E-01,-6.739112E-01,-6.440399E-01,-6.144279E-01,-5.839936E-01,-5.552226E-01,-5.258458E-01,-4.963136E-01,-4.661083E-01,-4.372565E-01,-4.071385E-01,-3.772034E-01,-3.474149E-01,-3.179872E-01,-2.884477E-01,-2.591692E-01,-2.296466E-01,-1.992789E-01,-1.704972E-01,-1.405461E-01,-1.121859E-01,+1.007662E-01,+1.298766E-01,+1.599123E-01,+1.894308E-01,+2.192970E-01,+2.485739E-01,+2.784417E-01,+3.083025E-01,+3.378269E-01,+3.675199E-01,+3.977209E-01,+4.274913E-01,+4.563614E-01,+4.861338E-01,+5.155823E-01,+5.452126E-01,+5.746472E-01,+6.042270E-01,+6.339468E-01,+6.638836E-01,+6.940050E-01,+7.230136E-01,+7.526349E-01,+7.820225E-01,+8.115408E-01,+8.403910E-01,+8.709495E-01,+9.001150E-01,+9.304963E-01,+9.595184E-01,+9.894642E-01,+1.019160E+00,+1.049015E+00,+1.078787E+00,+1.108224E+00,+1.137700E+00,+1.167495E+00,+1.196685E+00,+1.226745E+00,+1.256374E+00,+1.285946E+00,+1.315338E+00,+1.344977E+00,+1.374636E+00,+1.403895E+00,+1.433721E+00,+1.463382E+00,+1.492778E+00,+1.522410E+00,+1.552002E+00,+1.581552E+00,+1.611414E+00,+1.640699E+00,+1.670405E+00,+1.700358E+00,+1.730019E+00,+1.759334E+00,+1.788754E+00,+1.818605E+00,+1.847959E+00,+1.878009E+00,+1.907288E+00,+1.937388E+00,+1.967391E+00,+1.996346E+00,+2.025945E+00,+2.056296E+00,+2.086158E+00,+2.115032E+00,+2.144713E+00,+2.173996E+00,+2.203293E+00,+2.232911E+00,+2.262591E+00,+2.292855E+00,+2.321950E+00,+2.352022E+00,+2.380919E+00,+2.411301E+00,+2.440711E+00,+2.470657E+00,+2.499519E+00,+2.529215E+00,+2.559143E+00,+2.588974E+00,+2.618069E+00,+2.647741E+00,+2.678497E+00,+2.707305E+00,+2.737102E+00,+2.766890E+00,+2.796456E+00,+2.826030E+00,+2.848320E+00,+2.846254E+00,+2.843380E+00,+2.845234E+00,+2.843540E+00,+2.840518E+00};
// double[] I_4={-2.081751E-06,-2.009404E-06,-1.994022E-06,-1.973836E-06,-1.929839E-06,-1.860480E-06,-1.853168E-06,-1.827094E-06,-1.752273E-06,-1.748335E-06,-1.745498E-06,-1.732158E-06,-1.697962E-06,-1.668940E-06,-1.691216E-06,-1.653912E-06,-1.615770E-06,-1.589121E-06,-1.578496E-06,-1.511848E-06,-1.490693E-06,-1.554045E-06,-1.524058E-06,-1.460129E-06,-1.445713E-06,-1.428716E-06,-1.362101E-06,-1.351600E-06,-1.364955E-06,-1.339278E-06,-1.323781E-06,-1.300190E-06,-1.315574E-06,-1.286688E-06,-1.245205E-06,-1.227854E-06,-1.208845E-06,-1.154057E-06,-1.127390E-06,-1.185431E-06,-1.143140E-06,-1.110089E-06,-1.100546E-06,-1.068139E-06,-1.010646E-06,-9.913610E-07,-1.001900E-06,-9.895894E-07,-9.709853E-07,-9.424954E-07,-9.676421E-07,-9.217236E-07,-8.865008E-07,-8.736729E-07,-8.604692E-07,-7.934345E-07,-7.845197E-07,-7.870328E-07,-7.897173E-07,-7.594688E-07,-7.373922E-07,-7.608459E-07,-7.289285E-07,-6.863308E-07,-6.667792E-07,-6.560932E-07,-5.891535E-07,-5.753902E-07,-5.805540E-07,-5.761904E-07,-5.538917E-07,-5.759023E-07,-5.574636E-07,-5.186790E-07,-4.836601E-07,-4.666182E-07,-4.503912E-07,-3.893794E-07,-3.883029E-07,-3.937614E-07,-3.758637E-07,-3.520073E-07,-3.444236E-07,-3.600272E-07,-3.200202E-07,-2.871529E-07,-2.707148E-07,-2.554138E-07,-1.945607E-07,-1.923971E-07,-1.989398E-07,-1.833098E-07,-1.640765E-07,-1.841795E-07,-1.636639E-07,-1.292935E-07,-9.333970E-08,-8.491151E-08,-6.956358E-08,+1.007662E-07,+1.186156E-07,+1.221656E-07,+1.533130E-07,+2.036568E-07,+2.786030E-07,+3.463839E-07,+5.630524E-07,+9.055847E-07,+1.463823E-06,+2.416522E-06,+3.995021E-06,+6.343181E-06,+9.961782E-06,+1.520680E-05,+2.282994E-05,+3.265913E-05,+4.547259E-05,+6.128139E-05,+8.003513E-05,+1.012809E-04,+1.256682E-04,+1.525581E-04,+1.813834E-04,+2.126781E-04,+2.460281E-04,+2.810921E-04,+3.178513E-04,+3.556531E-04,+3.960776E-04,+4.367381E-04,+4.808895E-04,+5.246410E-04,+5.702397E-04,+6.183776E-04,+6.664080E-04,+7.175178E-04,+7.685868E-04,+8.218214E-04,+8.777060E-04,+9.333867E-04,+9.924220E-04,+1.046103E-03,+1.109118E-03,+1.172512E-03,+1.240784E-03,+1.310312E-03,+1.385549E-03,+1.457680E-03,+1.547039E-03,+1.637857E-03,+1.739415E-03,+1.833796E-03,+1.950836E-03,+2.095960E-03,+2.220956E-03,+2.379380E-03,+2.604725E-03,+2.789510E-03,+3.095475E-03,+3.372518E-03,+3.715479E-03,+4.194421E-03,+4.607261E-03,+5.405699E-03,+5.824457E-03,+6.508712E-03,+7.148860E-03,+7.725883E-03,+8.383042E-03,+9.115150E-03,+9.717179E-03,+1.054938E-02,+1.125623E-02,+1.199603E-02,+1.260986E-02,+1.336380E-02,+1.397413E-02,+1.476659E-02,+1.558602E-02,+1.638944E-02,+1.721052E-02,+1.807261E-02,+1.900422E-02,+1.991968E-02,+2.092345E-02,+2.186724E-02,+2.275597E-02,+2.377400E-02,+2.501705E-02,+2.601024E-02,+2.738734E-02,+2.853431E-02,+2.962430E-02,+2.962492E-02,+2.962452E-02,+2.962472E-02,+2.962615E-02,+2.962345E-02};
// 
// 
// int I4len=I_4.length;
// for(int i=0;i<I4len;i++)
// {I_4[i]=I_4[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_5={-3.013682E+00,-2.984322E+00,-2.954129E+00,-2.925464E+00,-2.895110E+00,-2.865177E+00,-2.835801E+00,-2.806856E+00,-2.777095E+00,-2.747483E+00,-2.717695E+00,-2.687858E+00,-2.658668E+00,-2.628698E+00,-2.599252E+00,-2.569173E+00,-2.539376E+00,-2.510230E+00,-2.480851E+00,-2.451072E+00,-2.421260E+00,-2.391795E+00,-2.362195E+00,-2.332304E+00,-2.303170E+00,-2.272859E+00,-2.243876E+00,-2.213922E+00,-2.184423E+00,-2.154661E+00,-2.125252E+00,-2.095792E+00,-2.065763E+00,-2.035789E+00,-2.006022E+00,-1.976980E+00,-1.947226E+00,-1.917177E+00,-1.888532E+00,-1.858348E+00,-1.829069E+00,-1.799623E+00,-1.769271E+00,-1.740589E+00,-1.711061E+00,-1.680994E+00,-1.651520E+00,-1.621624E+00,-1.591771E+00,-1.562356E+00,-1.532347E+00,-1.503288E+00,-1.473328E+00,-1.443946E+00,-1.413739E+00,-1.384562E+00,-1.355013E+00,-1.325616E+00,-1.296206E+00,-1.265658E+00,-1.236313E+00,-1.206458E+00,-1.177171E+00,-1.147521E+00,-1.118120E+00,-1.088499E+00,-1.058810E+00,-1.029461E+00,-9.992237E-01,-9.698748E-01,-9.399538E-01,-9.101675E-01,-8.810193E-01,-8.511023E-01,-8.217560E-01,-7.921158E-01,-7.630769E-01,-7.329741E-01,-7.035236E-01,-6.740128E-01,-6.442024E-01,-6.141025E-01,-5.848002E-01,-5.545553E-01,-5.260924E-01,-4.957531E-01,-4.659697E-01,-4.372888E-01,-4.071604E-01,-3.777797E-01,-3.480198E-01,-3.186417E-01,-2.883701E-01,-2.591814E-01,-2.294890E-01,-1.996192E-01,-1.701692E-01,-1.401394E-01,-1.101974E-01,+9.959906E-02,+1.298809E-01,+1.595775E-01,+1.892729E-01,+2.191346E-01,+2.482486E-01,+2.781156E-01,+3.077248E-01,+3.382667E-01,+3.677065E-01,+3.974086E-01,+4.270253E-01,+4.563767E-01,+4.858034E-01,+5.154884E-01,+5.452996E-01,+5.742339E-01,+6.042636E-01,+6.338719E-01,+6.636556E-01,+6.933563E-01,+7.230476E-01,+7.527436E-01,+7.814491E-01,+8.118183E-01,+8.411756E-01,+8.699076E-01,+9.001635E-01,+9.298415E-01,+9.597944E-01,+9.895609E-01,+1.018923E+00,+1.048946E+00,+1.078475E+00,+1.108110E+00,+1.137419E+00,+1.167169E+00,+1.196690E+00,+1.226486E+00,+1.255816E+00,+1.285462E+00,+1.315374E+00,+1.345106E+00,+1.374144E+00,+1.403729E+00,+1.433769E+00,+1.462891E+00,+1.492858E+00,+1.521837E+00,+1.551710E+00,+1.581845E+00,+1.611487E+00,+1.640882E+00,+1.670662E+00,+1.700024E+00,+1.730066E+00,+1.759393E+00,+1.789375E+00,+1.818272E+00,+1.847896E+00,+1.877726E+00,+1.907654E+00,+1.937383E+00,+1.966693E+00,+1.996026E+00,+2.025348E+00,+2.055753E+00,+2.084712E+00,+2.115118E+00,+2.144731E+00,+2.174093E+00,+2.203373E+00,+2.232804E+00,+2.263547E+00,+2.292635E+00,+2.322356E+00,+2.351521E+00,+2.381003E+00,+2.410650E+00,+2.440594E+00,+2.470107E+00,+2.498818E+00,+2.529315E+00,+2.558878E+00,+2.588623E+00,+2.617252E+00,+2.647836E+00,+2.677544E+00,+2.707127E+00,+2.736675E+00,+2.766631E+00,+2.796478E+00,+2.826201E+00,+2.855250E+00,+2.884811E+00,+2.914482E+00,+2.943884E+00,+2.973601E+00,+3.003528E+00};
// double[] I_5={-6.126716E-07,-5.836260E-07,-5.759779E-07,-5.665485E-07,-5.542142E-07,-5.481432E-07,-5.396164E-07,-5.336939E-07,-5.270242E-07,-5.201840E-07,-5.067787E-07,-4.954545E-07,-4.888349E-07,-4.755563E-07,-4.696755E-07,-4.710999E-07,-4.652993E-07,-4.520299E-07,-4.385553E-07,-4.302555E-07,-4.268715E-07,-4.218363E-07,-4.191421E-07,-4.128245E-07,-4.107501E-07,-4.033622E-07,-3.982524E-07,-3.979738E-07,-3.863426E-07,-3.677581E-07,-3.593526E-07,-3.553046E-07,-3.520097E-07,-3.470976E-07,-3.448893E-07,-3.416754E-07,-3.369669E-07,-3.264672E-07,-3.258219E-07,-3.060127E-07,-2.994645E-07,-2.967118E-07,-2.924595E-07,-2.841898E-07,-2.848988E-07,-2.819035E-07,-2.810788E-07,-2.776947E-07,-2.680619E-07,-2.615882E-07,-2.492880E-07,-2.443198E-07,-2.370322E-07,-2.338592E-07,-2.303124E-07,-2.254356E-07,-2.238195E-07,-2.213809E-07,-2.192597E-07,-2.112489E-07,-2.026182E-07,-1.930683E-07,-1.859766E-07,-1.781844E-07,-1.745081E-07,-1.706280E-07,-1.690026E-07,-1.688380E-07,-1.681222E-07,-1.622853E-07,-1.574097E-07,-1.524870E-07,-1.427632E-07,-1.343429E-07,-1.271716E-07,-1.229548E-07,-1.205758E-07,-1.177413E-07,-1.183661E-07,-1.171181E-07,-1.131087E-07,-1.066880E-07,-9.959370E-08,-8.888067E-08,-8.374771E-08,-7.882999E-08,-7.607669E-08,-7.157455E-08,-7.261487E-08,-6.998392E-08,-6.798565E-08,-6.381175E-08,-5.659796E-08,-5.117728E-08,-4.175183E-08,-3.461846E-08,-2.798617E-08,-2.168824E-08,-1.847746E-08,+3.532680E-08,+4.925799E-08,+7.011234E-08,+1.050218E-07,+1.652060E-07,+2.620123E-07,+4.243890E-07,+6.764273E-07,+1.058888E-06,+1.687462E-06,+2.627368E-06,+4.002434E-06,+6.097730E-06,+9.059267E-06,+1.300816E-05,+1.859082E-05,+2.582927E-05,+3.491449E-05,+4.598387E-05,+5.864752E-05,+7.355149E-05,+9.028044E-05,+1.091370E-04,+1.293087E-04,+1.511690E-04,+1.747166E-04,+2.005359E-04,+2.283484E-04,+2.576146E-04,+2.891145E-04,+3.232152E-04,+3.587711E-04,+3.963305E-04,+4.352312E-04,+4.786001E-04,+5.227000E-04,+5.715849E-04,+6.227653E-04,+6.795725E-04,+7.385330E-04,+8.062837E-04,+8.758219E-04,+9.572108E-04,+1.037845E-03,+1.131167E-03,+1.233777E-03,+1.349777E-03,+1.473714E-03,+1.615221E-03,+1.763921E-03,+1.925149E-03,+2.098211E-03,+2.314844E-03,+2.524803E-03,+2.760829E-03,+2.991829E-03,+3.231254E-03,+3.488577E-03,+3.750780E-03,+4.044802E-03,+4.369530E-03,+4.748238E-03,+5.045746E-03,+5.410804E-03,+5.633759E-03,+5.965893E-03,+6.332260E-03,+6.759716E-03,+7.093353E-03,+7.428980E-03,+7.724902E-03,+8.105950E-03,+8.464211E-03,+8.882306E-03,+9.246616E-03,+9.761823E-03,+1.015300E-02,+1.061124E-02,+1.138344E-02,+1.188057E-02,+1.250652E-02,+1.300458E-02,+1.361336E-02,+1.413865E-02,+1.478617E-02,+1.529344E-02,+1.597527E-02,+1.662798E-02,+1.722475E-02,+1.790513E-02,+1.848814E-02,+1.916859E-02,+1.983513E-02,+2.065151E-02,+2.127573E-02,+2.187801E-02,+2.271373E-02,+2.347382E-02,+2.425946E-02};
// 
// int I5len=I_5.length;
// for(int i=0;i<I5len;i++)
// {I_5[i]=I_5[i]*(1+0.01*Math.random());}
// 
// 
// 
// double[] V_6={-3.013358E+00,-2.988498E+00,-2.962974E+00,-2.936950E+00,-2.911823E+00,-2.886607E+00,-2.861250E+00,-2.835584E+00,-2.810811E+00,-2.784249E+00,-2.759893E+00,-2.734341E+00,-2.709476E+00,-2.683989E+00,-2.658185E+00,-2.632876E+00,-2.607923E+00,-2.581978E+00,-2.557389E+00,-2.531831E+00,-2.506358E+00,-2.480734E+00,-2.455043E+00,-2.429619E+00,-2.404932E+00,-2.379803E+00,-2.353933E+00,-2.328107E+00,-2.302781E+00,-2.277362E+00,-2.253082E+00,-2.227125E+00,-2.201390E+00,-2.176279E+00,-2.150596E+00,-2.125083E+00,-2.100240E+00,-2.075049E+00,-2.049428E+00,-2.024280E+00,-1.998240E+00,-1.973413E+00,-1.948390E+00,-1.922796E+00,-1.897214E+00,-1.871807E+00,-1.846487E+00,-1.820737E+00,-1.795893E+00,-1.769450E+00,-1.744581E+00,-1.719724E+00,-1.693988E+00,-1.669070E+00,-1.643752E+00,-1.618457E+00,-1.592103E+00,-1.567308E+00,-1.541919E+00,-1.516801E+00,-1.491517E+00,-1.465987E+00,-1.440650E+00,-1.415534E+00,-1.390002E+00,-1.364802E+00,-1.339150E+00,-1.313665E+00,-1.288687E+00,-1.262872E+00,-1.237514E+00,-1.211974E+00,-1.186342E+00,-1.161616E+00,-1.136137E+00,-1.110552E+00,-1.085354E+00,-1.060074E+00,-1.034459E+00,-1.009005E+00,-9.837324E-01,-9.583952E-01,-9.336867E-01,-9.080087E-01,-8.818060E-01,-8.569614E-01,-8.312709E-01,-8.068879E-01,-7.807240E-01,-7.560344E-01,-7.299466E-01,-7.045795E-01,-6.792361E-01,-6.537949E-01,-6.288543E-01,-6.024001E-01,-5.776190E-01,-5.522770E-01,-5.276595E-01,-5.019723E-01,-4.766953E-01,-4.508348E-01,-4.258001E-01,-3.994645E-01,-3.748426E-01,-3.490878E-01,-3.249789E-01,-2.985389E-01,-2.727529E-01,-2.477397E-01,-2.222120E-01,-1.971868E-01,-1.719963E-01,-1.459697E-01,-1.213560E-01,-9.617317E-02,+1.118560E-01,+1.371226E-01,+1.630701E-01,+1.876740E-01,+2.135357E-01,+2.396470E-01,+2.642504E-01,+2.895941E-01,+3.156281E-01,+3.404916E-01,+3.658344E-01,+3.919573E-01,+4.163945E-01,+4.420856E-01,+4.679466E-01,+4.930406E-01,+5.184878E-01,+5.430182E-01,+5.694639E-01,+5.944759E-01,+6.205130E-01,+6.448350E-01,+6.708158E-01,+6.961643E-01,+7.212619E-01,+7.471930E-01,+7.718929E-01,+7.976779E-01,+8.222349E-01,+8.478033E-01,+8.731431E-01,+8.990367E-01,+9.242800E-01,+9.492651E-01,+9.747570E-01,+1.000108E+00,+1.025798E+00,+1.051225E+00,+1.076248E+00,+1.101915E+00,+1.126871E+00,+1.152553E+00,+1.178049E+00,+1.203732E+00,+1.228790E+00,+1.253630E+00,+1.279169E+00,+1.303951E+00,+1.330502E+00,+1.354849E+00,+1.380910E+00,+1.406230E+00,+1.431700E+00,+1.456861E+00,+1.482105E+00,+1.507762E+00,+1.532714E+00,+1.558049E+00,+1.583667E+00,+1.608597E+00,+1.634088E+00,+1.660062E+00,+1.684919E+00,+1.710503E+00,+1.735776E+00,+1.760907E+00,+1.786621E+00,+1.811536E+00,+1.837216E+00,+1.863015E+00,+1.888257E+00,+1.913647E+00,+1.938324E+00,+1.963312E+00,+1.989095E+00,+2.014576E+00,+2.040142E+00,+2.066166E+00,+2.091050E+00,+2.115907E+00,+2.142182E+00,+2.166759E+00,+2.192563E+00,+2.218376E+00,+2.243292E+00,+2.269150E+00,+2.293945E+00,+2.318833E+00,+2.344023E+00,+2.370432E+00,+2.395221E+00,+2.421561E+00,+2.445951E+00,+2.471709E+00,+2.497516E+00,+2.522523E+00,+2.548231E+00,+2.572952E+00,+2.597997E+00,+2.623569E+00,+2.649123E+00,+2.674665E+00,+2.699801E+00,+2.725434E+00,+2.750819E+00,+2.776418E+00,+2.801840E+00,+2.826557E+00,+2.852472E+00,+2.877419E+00,+2.902300E+00,+2.928481E+00,+2.954009E+00,+2.979493E+00,+3.004160E+00};
// double[] I_6={-9.791223E-06,-9.687346E-06,-9.559308E-06,-9.469348E-06,-9.406642E-06,-9.328548E-06,-9.191041E-06,-9.120414E-06,-9.051975E-06,-8.931408E-06,-8.830356E-06,-8.804134E-06,-8.729854E-06,-8.602770E-06,-8.517070E-06,-8.483799E-06,-8.388144E-06,-8.285642E-06,-8.204372E-06,-8.179572E-06,-8.074180E-06,-8.047622E-06,-7.958851E-06,-7.860479E-06,-7.748780E-06,-7.663916E-06,-7.565558E-06,-7.497262E-06,-7.327409E-06,-7.248138E-06,-7.207718E-06,-7.142692E-06,-7.035822E-06,-6.986620E-06,-6.898180E-06,-6.778211E-06,-6.700433E-06,-6.609914E-06,-6.576855E-06,-6.411069E-06,-6.373582E-06,-6.302637E-06,-6.219560E-06,-6.104286E-06,-6.098821E-06,-5.998022E-06,-5.929329E-06,-5.812171E-06,-5.734344E-06,-5.602342E-06,-5.536825E-06,-5.481189E-06,-5.428326E-06,-5.343027E-06,-5.257138E-06,-5.197764E-06,-5.141198E-06,-5.034862E-06,-4.949198E-06,-4.876010E-06,-4.761409E-06,-4.683801E-06,-4.600288E-06,-4.555967E-06,-4.474216E-06,-4.412836E-06,-4.355176E-06,-4.258194E-06,-4.145692E-06,-4.073512E-06,-3.988264E-06,-3.912222E-06,-3.784359E-06,-3.730043E-06,-3.680455E-06,-3.588901E-06,-3.514223E-06,-3.482420E-06,-3.390353E-06,-3.300099E-06,-3.215105E-06,-3.134053E-06,-3.048745E-06,-2.973758E-06,-2.927759E-06,-2.829749E-06,-2.735787E-06,-2.658752E-06,-2.579934E-06,-2.445268E-06,-2.367009E-06,-2.332862E-06,-2.252415E-06,-2.170252E-06,-2.092052E-06,-2.014896E-06,-1.890378E-06,-1.830997E-06,-1.773156E-06,-1.690549E-06,-1.611714E-06,-1.560364E-06,-1.478790E-06,-1.378399E-06,-1.274498E-06,-1.199327E-06,-1.119701E-06,-9.871219E-07,-9.291534E-07,-8.532841E-07,-7.757403E-07,-6.922393E-07,-6.453198E-07,-5.444252E-07,-4.415941E-07,-3.505775E-07,+4.897974E-07,+6.511236E-07,+8.574734E-07,+1.071160E-06,+1.344441E-06,+1.735626E-06,+2.262352E-06,+2.889519E-06,+3.790708E-06,+4.913314E-06,+6.301259E-06,+8.042716E-06,+1.001512E-05,+1.235296E-05,+1.523996E-05,+1.851842E-05,+2.245488E-05,+2.675762E-05,+3.200701E-05,+3.783351E-05,+4.492985E-05,+5.280332E-05,+6.192451E-05,+7.221141E-05,+8.410038E-05,+9.789787E-05,+1.134609E-04,+1.313295E-04,+1.512435E-04,+1.737522E-04,+1.994099E-04,+2.292802E-04,+2.617946E-04,+2.986169E-04,+3.396886E-04,+3.856295E-04,+4.366066E-04,+4.948464E-04,+5.579883E-04,+6.293258E-04,+7.076098E-04,+7.949182E-04,+8.893922E-04,+9.973100E-04,+1.106345E-03,+1.232109E-03,+1.372421E-03,+1.526239E-03,+1.697973E-03,+1.880507E-03,+2.079165E-03,+2.293392E-03,+2.527746E-03,+2.769295E-03,+3.048745E-03,+3.340999E-03,+3.637984E-03,+3.979328E-03,+4.331104E-03,+4.690330E-03,+5.080511E-03,+5.487330E-03,+5.906057E-03,+6.342591E-03,+6.813402E-03,+7.291309E-03,+7.763252E-03,+8.303922E-03,+8.791046E-03,+9.320207E-03,+9.882589E-03,+1.047618E-02,+1.102537E-02,+1.162190E-02,+1.221466E-02,+1.276459E-02,+1.337198E-02,+1.400147E-02,+1.459940E-02,+1.522933E-02,+1.587452E-02,+1.654660E-02,+1.725248E-02,+1.786186E-02,+1.855049E-02,+1.920361E-02,+1.984744E-02,+2.058339E-02,+2.123483E-02,+2.190892E-02,+2.260096E-02,+2.350368E-02,+2.438987E-02,+2.498568E-02,+2.590771E-02,+2.659651E-02,+2.738452E-02,+2.799286E-02,+2.872677E-02,+2.948961E-02,+3.027349E-02,+3.087509E-02,+3.165310E-02,+3.235963E-02,+3.306005E-02,+3.376537E-02,+3.452461E-02,+3.518787E-02,+3.583544E-02,+3.662521E-02,+3.746024E-02,+3.837563E-02,+3.894179E-02,+3.977829E-02,+4.055107E-02};
// 
// //int I6len=I_6.length;
// //for(int i=0;i<I6len;i++)
// //{I_6[i]=I_6[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_7={-3.013355E+00,-2.988101E+00,-2.962430E+00,-2.937680E+00,-2.911926E+00,-2.886858E+00,-2.861128E+00,-2.835975E+00,-2.810553E+00,-2.784897E+00,-2.759717E+00,-2.734202E+00,-2.708705E+00,-2.683441E+00,-2.657989E+00,-2.632512E+00,-2.607036E+00,-2.581495E+00,-2.557141E+00,-2.531465E+00,-2.505858E+00,-2.480417E+00,-2.454859E+00,-2.429855E+00,-2.403842E+00,-2.379607E+00,-2.353599E+00,-2.328258E+00,-2.302895E+00,-2.277946E+00,-2.252339E+00,-2.226997E+00,-2.201262E+00,-2.176156E+00,-2.150635E+00,-2.125123E+00,-2.099974E+00,-2.074521E+00,-2.049192E+00,-2.023640E+00,-1.998630E+00,-1.972909E+00,-1.947762E+00,-1.922488E+00,-1.897613E+00,-1.871402E+00,-1.846762E+00,-1.821226E+00,-1.795999E+00,-1.770237E+00,-1.744617E+00,-1.719618E+00,-1.694621E+00,-1.668592E+00,-1.643151E+00,-1.617460E+00,-1.592173E+00,-1.567589E+00,-1.542024E+00,-1.516677E+00,-1.491320E+00,-1.465222E+00,-1.440522E+00,-1.415110E+00,-1.390289E+00,-1.364376E+00,-1.338965E+00,-1.312985E+00,-1.288054E+00,-1.262605E+00,-1.237881E+00,-1.212108E+00,-1.186374E+00,-1.161395E+00,-1.135803E+00,-1.110758E+00,-1.085021E+00,-1.060012E+00,-1.034265E+00,-1.009819E+00,-9.836185E-01,-9.578307E-01,-9.334113E-01,-9.072825E-01,-8.820918E-01,-8.570937E-01,-8.312597E-01,-8.064979E-01,-7.813755E-01,-7.558473E-01,-7.286856E-01,-7.037549E-01,-6.796440E-01,-6.536369E-01,-6.289915E-01,-6.024001E-01,-5.776151E-01,-5.523456E-01,-5.274856E-01,-5.017254E-01,-4.765252E-01,-4.508348E-01,-4.255613E-01,-4.004601E-01,-3.744406E-01,-3.497434E-01,-3.241339E-01,-2.987791E-01,-2.729995E-01,-2.479783E-01,-2.226201E-01,-1.975245E-01,-1.717449E-01,-1.463004E-01,-1.207803E-01,-9.567399E-02,+1.115156E-01,+1.367064E-01,+1.629011E-01,+1.880911E-01,+2.134522E-01,+2.390566E-01,+2.645823E-01,+2.896834E-01,+3.153716E-01,+3.406607E-01,+3.655964E-01,+3.913656E-01,+4.163083E-01,+4.424937E-01,+4.682834E-01,+4.930538E-01,+5.185677E-01,+5.432758E-01,+5.690507E-01,+5.935345E-01,+6.198165E-01,+6.451200E-01,+6.700155E-01,+6.954783E-01,+7.211640E-01,+7.472914E-01,+7.719399E-01,+7.969999E-01,+8.223515E-01,+8.477977E-01,+8.728270E-01,+8.983089E-01,+9.238505E-01,+9.497205E-01,+9.748404E-01,+1.000358E+00,+1.025215E+00,+1.051385E+00,+1.076513E+00,+1.101824E+00,+1.126848E+00,+1.152357E+00,+1.177115E+00,+1.203457E+00,+1.228631E+00,+1.253562E+00,+1.279379E+00,+1.304227E+00,+1.330168E+00,+1.355665E+00,+1.380641E+00,+1.406026E+00,+1.431783E+00,+1.456964E+00,+1.482301E+00,+1.507408E+00,+1.532609E+00,+1.558498E+00,+1.583939E+00,+1.609025E+00,+1.633982E+00,+1.660145E+00,+1.685264E+00,+1.710394E+00,+1.735501E+00,+1.760955E+00,+1.786943E+00,+1.812254E+00,+1.837099E+00,+1.861877E+00,+1.887776E+00,+1.913261E+00,+1.938908E+00,+1.963668E+00,+1.989552E+00,+2.014839E+00,+2.040059E+00,+2.065499E+00,+2.090424E+00,+2.115962E+00,+2.141290E+00,+2.166470E+00,+2.192401E+00,+2.217935E+00,+2.243338E+00,+2.268764E+00,+2.294470E+00,+2.318687E+00,+2.344555E+00,+2.370194E+00,+2.395189E+00,+2.420493E+00,+2.445601E+00,+2.471492E+00,+2.497483E+00,+2.522891E+00,+2.547848E+00,+2.573153E+00,+2.598164E+00,+2.623516E+00,+2.649272E+00,+2.674951E+00,+2.700040E+00,+2.724999E+00,+2.750792E+00,+2.775759E+00,+2.801988E+00,+2.826948E+00,+2.852087E+00,+2.877663E+00,+2.902692E+00,+2.927794E+00,+2.953696E+00,+2.980054E+00,+3.005161E+00};
// double[] I_7={-4.718221E-07,-4.340392E-07,-4.289122E-07,-4.324903E-07,-3.776495E-07,-3.945041E-07,-4.037576E-07,-3.950046E-07,-3.864156E-07,-4.220553E-07,-4.002570E-07,-3.726373E-07,-3.737784E-07,-3.182995E-07,-3.320714E-07,-3.486660E-07,-3.429060E-07,-3.356052E-07,-3.702333E-07,-3.559599E-07,-3.245136E-07,-3.218445E-07,-3.207666E-07,-2.641129E-07,-2.776873E-07,-2.996926E-07,-2.883507E-07,-3.233854E-07,-3.086320E-07,-2.746297E-07,-2.745046E-07,-2.698354E-07,-2.220363E-07,-2.976053E-07,-2.673385E-07,-2.440580E-07,-2.449289E-07,-2.044410E-07,-1.940172E-07,-2.172029E-07,-2.176631E-07,-2.110793E-07,-2.470674E-07,-2.303850E-07,-2.035986E-07,-2.005610E-07,-1.995237E-07,-1.457214E-07,-1.634432E-07,-1.768318E-07,-1.727100E-07,-1.655724E-07,-2.097737E-07,-1.880127E-07,-1.586514E-07,-1.590685E-07,-1.596010E-07,-1.128941E-07,-1.230316E-07,-1.379214E-07,-1.312902E-07,-1.690352E-07,-1.719534E-07,-1.434712E-07,-1.216545E-07,-1.232418E-07,-1.229449E-07,-6.848110E-08,-9.283797E-08,-1.079766E-07,-1.000915E-07,-9.579589E-08,-1.354617E-07,-1.116437E-07,-9.429579E-08,-9.237548E-08,-9.308758E-08,-4.596053E-08,-6.543392E-08,-7.965453E-08,-7.015122E-08,-7.507164E-08,-9.704443E-08,-6.735594E-08,-6.689717E-08,-6.793526E-08,-1.584782E-08,-4.070442E-08,-5.238086E-08,-4.908587E-08,-4.621014E-08,-8.895989E-08,-6.760573E-08,-4.537755E-08,-4.520799E-08,-4.716938E-08,+4.837788E-09,-2.026880E-08,-3.382281E-08,-2.861044E-08,-2.473127E-08,-7.023180E-08,-4.858673E-08,-2.439796E-08,-2.802675E-08,-2.919394E-08,+1.981004E-08,-2.669160E-09,-2.118603E-08,-1.176083E-08,-1.163563E-08,-5.138306E-08,-2.781784E-08,-1.167734E-08,-1.313736E-08,+3.557538E-08,+6.076223E-08,+3.498985E-08,+5.292408E-08,+7.127443E-08,+4.992231E-08,+9.350401E-08,+1.767913E-07,+2.769216E-07,+4.297261E-07,+6.952098E-07,+1.031185E-06,+1.566797E-06,+2.326814E-06,+3.435936E-06,+4.817463E-06,+6.698976E-06,+9.100501E-06,+1.193071E-05,+1.563722E-05,+2.007099E-05,+2.558505E-05,+3.190024E-05,+3.982476E-05,+4.905299E-05,+6.027925E-05,+7.384914E-05,+8.967056E-05,+1.090735E-04,+1.314895E-04,+1.580112E-04,+1.896214E-04,+2.272755E-04,+2.701612E-04,+3.209290E-04,+3.786755E-04,+4.451647E-04,+5.208108E-04,+6.104453E-04,+7.088086E-04,+8.227184E-04,+9.489789E-04,+1.086878E-03,+1.246848E-03,+1.427458E-03,+1.627916E-03,+1.850858E-03,+2.097613E-03,+2.365267E-03,+2.669387E-03,+2.994048E-03,+3.353141E-03,+3.742937E-03,+4.158260E-03,+4.603946E-03,+5.088963E-03,+5.610366E-03,+6.143739E-03,+6.728717E-03,+7.331254E-03,+7.971929E-03,+8.635919E-03,+9.342123E-03,+1.004923E-02,+1.081081E-02,+1.156611E-02,+1.235179E-02,+1.319334E-02,+1.403942E-02,+1.489556E-02,+1.578254E-02,+1.666276E-02,+1.760830E-02,+1.856121E-02,+1.953116E-02,+2.050526E-02,+2.146590E-02,+2.248085E-02,+2.350400E-02,+2.451575E-02,+2.557580E-02,+2.665353E-02,+2.773003E-02,+2.882245E-02,+2.991918E-02,+3.107090E-02,+3.215382E-02,+3.330098E-02,+3.443791E-02,+3.558413E-02,+3.675866E-02,+3.791007E-02,+3.908131E-02,+4.025942E-02,+4.145275E-02,+4.265456E-02,+4.385197E-02,+4.503131E-02,+4.625988E-02,+4.744893E-02,+4.865804E-02,+4.987175E-02,+5.109341E-02,+5.237678E-02,+5.356742E-02,+5.482816E-02,+5.602108E-02,+5.731973E-02,+5.854754E-02,+5.979592E-02,+6.100322E-02,+6.229069E-02,+6.351186E-02,+6.485561E-02,+6.608967E-02,+6.738423E-02};
// 
// int I7len=I_7.length;
// for(int i=0;i<I7len;i++)
// {I_7[i]=I_7[i]*(1+0.01*Math.random());}
// 
// 
// double[] V_8={-3.013567E+00,-2.988962E+00,-2.962875E+00,-2.937323E+00,-2.912348E+00,-2.886324E+00,-2.861128E+00,-2.836085E+00,-2.810830E+00,-2.784879E+00,-2.759216E+00,-2.734471E+00,-2.709836E+00,-2.683293E+00,-2.658835E+00,-2.633245E+00,-2.607005E+00,-2.582377E+00,-2.557090E+00,-2.531162E+00,-2.506058E+00,-2.480767E+00,-2.454794E+00,-2.429936E+00,-2.404278E+00,-2.379807E+00,-2.353281E+00,-2.328592E+00,-2.303130E+00,-2.277711E+00,-2.253173E+00,-2.227959E+00,-2.201399E+00,-2.176239E+00,-2.150923E+00,-2.126431E+00,-2.100102E+00,-2.074938E+00,-2.049567E+00,-2.023724E+00,-1.998657E+00,-1.972619E+00,-1.948430E+00,-1.922501E+00,-1.896996E+00,-1.871549E+00,-1.845761E+00,-1.821164E+00,-1.795844E+00,-1.770130E+00,-1.744903E+00,-1.719474E+00,-1.693927E+00,-1.669104E+00,-1.644220E+00,-1.618051E+00,-1.592259E+00,-1.567663E+00,-1.542013E+00,-1.517051E+00,-1.491173E+00,-1.465158E+00,-1.440964E+00,-1.415246E+00,-1.390132E+00,-1.364701E+00,-1.339445E+00,-1.313793E+00,-1.288480E+00,-1.262588E+00,-1.237756E+00,-1.212415E+00,-1.187343E+00,-1.161077E+00,-1.135766E+00,-1.111251E+00,-1.086069E+00,-1.059859E+00,-1.033556E+00,-1.008879E+00,-9.836185E-01,-9.588828E-01,-9.329611E-01,-9.079376E-01,-8.827590E-01,-8.567227E-01,-8.317434E-01,-8.065759E-01,-7.810734E-01,-7.561962E-01,-7.298435E-01,-7.045199E-01,-6.796303E-01,-6.540144E-01,-6.285618E-01,-6.025669E-01,-5.781078E-01,-5.525995E-01,-5.266586E-01,-5.015519E-01,-4.770322E-01,-4.508348E-01,-4.249946E-01,-3.999542E-01,-3.745140E-01,-3.496482E-01,-3.248846E-01,-2.986123E-01,-2.731718E-01,-2.478215E-01,-2.227095E-01,-1.973550E-01,-1.719152E-01,-1.458882E-01,-1.206977E-01,-9.567527E-02,+1.116892E-01,+1.373820E-01,+1.629878E-01,+1.883464E-01,+2.136248E-01,+2.393085E-01,+2.642433E-01,+2.896155E-01,+3.151107E-01,+3.406470E-01,+3.660135E-01,+3.913814E-01,+4.164780E-01,+4.419992E-01,+4.675846E-01,+4.933808E-01,+5.185713E-01,+5.435984E-01,+5.688762E-01,+5.938801E-01,+6.199374E-01,+6.456205E-01,+6.705565E-01,+6.956452E-01,+7.212619E-01,+7.472965E-01,+7.725028E-01,+7.970940E-01,+8.223570E-01,+8.477085E-01,+8.730538E-01,+8.988989E-01,+9.243883E-01,+9.491817E-01,+9.748142E-01,+1.000067E+00,+1.025284E+00,+1.050996E+00,+1.076596E+00,+1.102498E+00,+1.127053E+00,+1.152577E+00,+1.177584E+00,+1.203032E+00,+1.228814E+00,+1.253605E+00,+1.279186E+00,+1.304411E+00,+1.330269E+00,+1.355460E+00,+1.380901E+00,+1.406035E+00,+1.430686E+00,+1.456434E+00,+1.482281E+00,+1.508012E+00,+1.533620E+00,+1.558675E+00,+1.583218E+00,+1.608680E+00,+1.634694E+00,+1.659639E+00,+1.685597E+00,+1.710097E+00,+1.735348E+00,+1.761467E+00,+1.786490E+00,+1.812014E+00,+1.837157E+00,+1.863086E+00,+1.888052E+00,+1.913512E+00,+1.939171E+00,+1.963821E+00,+1.989675E+00,+2.015035E+00,+2.039989E+00,+2.065457E+00,+2.091008E+00,+2.115726E+00,+2.141417E+00,+2.167010E+00,+2.192293E+00,+2.218450E+00,+2.243065E+00,+2.268323E+00,+2.293710E+00,+2.319052E+00,+2.344440E+00,+2.370198E+00,+2.395687E+00,+2.420198E+00,+2.445835E+00,+2.470992E+00,+2.497283E+00,+2.522824E+00,+2.547812E+00,+2.572535E+00,+2.598129E+00,+2.623587E+00,+2.649862E+00,+2.674408E+00,+2.700726E+00,+2.724905E+00,+2.750412E+00,+2.776084E+00,+2.801461E+00,+2.826512E+00,+2.852395E+00,+2.877380E+00,+2.902545E+00,+2.928172E+00,+2.954049E+00,+2.979076E+00,+3.004329E+00};
// double[] I_8={-1.004119E-05,-9.937182E-06,-9.818390E-06,-9.699905E-06,-9.659393E-06,-9.546831E-06,-9.463259E-06,-9.407601E-06,-9.365541E-06,-9.213921E-06,-9.092385E-06,-9.030852E-06,-8.953336E-06,-8.805760E-06,-8.722624E-06,-8.686899E-06,-8.608607E-06,-8.532105E-06,-8.456513E-06,-8.409585E-06,-8.311681E-06,-8.201370E-06,-8.083030E-06,-8.031336E-06,-7.892979E-06,-7.798451E-06,-7.740397E-06,-7.680758E-06,-7.603724E-06,-7.513936E-06,-7.489905E-06,-7.370908E-06,-7.274384E-06,-7.163964E-06,-7.065898E-06,-6.985619E-06,-6.871687E-06,-6.825251E-06,-6.734642E-06,-6.669981E-06,-6.582281E-06,-6.496323E-06,-6.434764E-06,-6.347557E-06,-6.245500E-06,-6.147324E-06,-6.080678E-06,-6.018126E-06,-5.872235E-06,-5.796526E-06,-5.733767E-06,-5.644053E-06,-5.558521E-06,-5.484812E-06,-5.447578E-06,-5.349937E-06,-5.252686E-06,-5.180444E-06,-5.102941E-06,-5.026072E-06,-4.891042E-06,-4.835355E-06,-4.750236E-06,-4.669736E-06,-4.598463E-06,-4.517667E-06,-4.489743E-06,-4.390459E-06,-4.291929E-06,-4.193264E-06,-4.126035E-06,-4.034669E-06,-3.967486E-06,-3.835599E-06,-3.762015E-06,-3.710216E-06,-3.635215E-06,-3.549185E-06,-3.461465E-06,-3.414133E-06,-3.340024E-06,-3.238148E-06,-3.141414E-06,-3.073892E-06,-2.982555E-06,-2.852934E-06,-2.780068E-06,-2.716501E-06,-2.644422E-06,-2.559356E-06,-2.463493E-06,-2.431369E-06,-2.328993E-06,-2.248513E-06,-2.159180E-06,-2.077288E-06,-1.959569E-06,-1.865284E-06,-1.801308E-06,-1.726446E-06,-1.639418E-06,-1.553149E-06,-1.515759E-06,-1.419358E-06,-1.321434E-06,-1.223185E-06,-1.141225E-06,-1.054860E-06,-9.287426E-07,-8.630794E-07,-7.978757E-07,-7.105531E-07,-6.240581E-07,-5.370089E-07,-4.880041E-07,-3.804490E-07,+5.099832E-07,+6.614273E-07,+8.497635E-07,+1.116274E-06,+1.411250E-06,+1.782469E-06,+2.279057E-06,+2.966307E-06,+3.864944E-06,+4.912023E-06,+6.299802E-06,+8.037082E-06,+1.006562E-05,+1.244421E-05,+1.536040E-05,+1.871010E-05,+2.254246E-05,+2.691676E-05,+3.207603E-05,+3.796287E-05,+4.491960E-05,+5.260680E-05,+6.158043E-05,+7.158597E-05,+8.340305E-05,+9.685117E-05,+1.124137E-04,+1.295486E-04,+1.486315E-04,+1.706344E-04,+1.952881E-04,+2.237113E-04,+2.545530E-04,+2.904013E-04,+3.299621E-04,+3.745331E-04,+4.244603E-04,+4.821068E-04,+5.443371E-04,+6.148283E-04,+6.909579E-04,+7.795240E-04,+8.748847E-04,+9.831076E-04,+1.094272E-03,+1.222492E-03,+1.364724E-03,+1.516322E-03,+1.692615E-03,+1.877000E-03,+2.085574E-03,+2.304106E-03,+2.540671E-03,+2.795843E-03,+3.073080E-03,+3.372904E-03,+3.688287E-03,+4.024144E-03,+4.377477E-03,+4.755015E-03,+5.148476E-03,+5.581744E-03,+6.002563E-03,+6.451193E-03,+6.914741E-03,+7.396708E-03,+7.909752E-03,+8.413762E-03,+8.923790E-03,+9.464071E-03,+9.989409E-03,+1.060268E-02,+1.115182E-02,+1.176216E-02,+1.234987E-02,+1.296869E-02,+1.360855E-02,+1.420721E-02,+1.486619E-02,+1.548321E-02,+1.614550E-02,+1.681810E-02,+1.747353E-02,+1.816525E-02,+1.884443E-02,+1.952284E-02,+2.019790E-02,+2.087589E-02,+2.164814E-02,+2.235323E-02,+2.310731E-02,+2.385668E-02,+2.461266E-02,+2.533632E-02,+2.608555E-02,+2.687189E-02,+2.763174E-02,+2.835194E-02,+2.912677E-02,+2.986850E-02,+3.064216E-02,+3.135240E-02,+3.208165E-02,+3.285660E-02,+3.355920E-02,+3.439681E-02,+3.506477E-02,+3.576639E-02,+3.643629E-02,+3.723528E-02,+3.804137E-02,+3.867013E-02,+3.947449E-02,+4.011193E-02,+4.103834E-02};
// 
// double[] V_9={-3.013376E+00,-2.988518E+00,-2.962641E+00,-2.936763E+00,-2.911862E+00,-2.886472E+00,-2.861404E+00,-2.835565E+00,-2.810747E+00,-2.785898E+00,-2.759096E+00,-2.733896E+00,-2.709002E+00,-2.683656E+00,-2.658197E+00,-2.632340E+00,-2.606970E+00,-2.581832E+00,-2.556405E+00,-2.531514E+00,-2.506241E+00,-2.480901E+00,-2.454826E+00,-2.430053E+00,-2.404978E+00,-2.379437E+00,-2.354012E+00,-2.328019E+00,-2.302567E+00,-2.277294E+00,-2.252635E+00,-2.227232E+00,-2.201405E+00,-2.175576E+00,-2.150592E+00,-2.125235E+00,-2.100073E+00,-2.075008E+00,-2.048719E+00,-2.023514E+00,-1.999077E+00,-1.973747E+00,-1.947679E+00,-1.922598E+00,-1.896431E+00,-1.871615E+00,-1.846237E+00,-1.820796E+00,-1.795940E+00,-1.770249E+00,-1.744845E+00,-1.719940E+00,-1.694082E+00,-1.669070E+00,-1.643419E+00,-1.617627E+00,-1.592498E+00,-1.566974E+00,-1.542086E+00,-1.516614E+00,-1.491320E+00,-1.465389E+00,-1.440365E+00,-1.415117E+00,-1.389650E+00,-1.364284E+00,-1.339307E+00,-1.313236E+00,-1.288287E+00,-1.262747E+00,-1.237890E+00,-1.212199E+00,-1.186323E+00,-1.161092E+00,-1.135591E+00,-1.110393E+00,-1.084959E+00,-1.059711E+00,-1.034689E+00,-1.009123E+00,-9.835285E-01,-9.589469E-01,-9.332238E-01,-9.080982E-01,-8.816806E-01,-8.566220E-01,-8.314713E-01,-8.065705E-01,-7.805730E-01,-7.555493E-01,-7.299171E-01,-7.045009E-01,-6.794543E-01,-6.538607E-01,-6.288955E-01,-6.032180E-01,-5.781912E-01,-5.522845E-01,-5.266480E-01,-5.018787E-01,-4.763681E-01,-4.504177E-01,-4.250780E-01,-4.004466E-01,-3.749159E-01,-3.498221E-01,-3.239824E-01,-2.984394E-01,-2.731627E-01,-2.481418E-01,-2.224458E-01,-1.972610E-01,-1.720693E-01,-1.461385E-01,-1.211998E-01,-9.567206E-02,+1.116914E-01,+1.368797E-01,+1.628967E-01,+1.884247E-01,+2.131948E-01,+2.377959E-01,+2.639966E-01,+2.896795E-01,+3.156303E-01,+3.408275E-01,+3.660233E-01,+3.916184E-01,+4.165474E-01,+4.416804E-01,+4.678537E-01,+4.928705E-01,+5.186546E-01,+5.436855E-01,+5.687736E-01,+5.937967E-01,+6.194870E-01,+6.450279E-01,+6.703582E-01,+6.958166E-01,+7.208158E-01,+7.468010E-01,+7.723985E-01,+7.972393E-01,+8.226239E-01,+8.474754E-01,+8.728153E-01,+8.993522E-01,+9.242215E-01,+9.493098E-01,+9.745836E-01,+1.000408E+00,+1.025645E+00,+1.051051E+00,+1.076201E+00,+1.101271E+00,+1.126810E+00,+1.152561E+00,+1.177767E+00,+1.203323E+00,+1.228540E+00,+1.254038E+00,+1.279313E+00,+1.304135E+00,+1.330538E+00,+1.355581E+00,+1.380901E+00,+1.405656E+00,+1.431404E+00,+1.456620E+00,+1.482562E+00,+1.507701E+00,+1.532609E+00,+1.558216E+00,+1.583438E+00,+1.608608E+00,+1.634915E+00,+1.660145E+00,+1.685419E+00,+1.710704E+00,+1.735455E+00,+1.760657E+00,+1.786836E+00,+1.811930E+00,+1.837207E+00,+1.862619E+00,+1.887892E+00,+1.913576E+00,+1.938837E+00,+1.964805E+00,+1.989495E+00,+2.014729E+00,+2.040350E+00,+2.065485E+00,+2.091162E+00,+2.116424E+00,+2.141135E+00,+2.166785E+00,+2.192789E+00,+2.218268E+00,+2.242898E+00,+2.268399E+00,+2.293846E+00,+2.318885E+00,+2.344289E+00,+2.369896E+00,+2.395218E+00,+2.420711E+00,+2.445801E+00,+2.471976E+00,+2.497333E+00,+2.522439E+00,+2.547760E+00,+2.572886E+00,+2.598379E+00,+2.623802E+00,+2.649296E+00,+2.674027E+00,+2.699866E+00,+2.724518E+00,+2.750412E+00,+2.775184E+00,+2.801356E+00,+2.826762E+00,+2.852536E+00,+2.877296E+00,+2.903187E+00,+2.928117E+00,+2.953926E+00,+2.979283E+00,+3.004079E+00};
// double[] I_9={-4.872983E-07,-4.244467E-07,-4.386657E-07,-4.609754E-07,-4.554763E-07,-4.469919E-07,-4.823223E-07,-4.384096E-07,-4.315756E-07,-4.248495E-07,-3.738517E-07,-3.931304E-07,-4.016750E-07,-3.916549E-07,-3.821852E-07,-4.223055E-07,-3.959168E-07,-3.658540E-07,-3.663030E-07,-3.139578E-07,-3.166270E-07,-3.376791E-07,-3.361936E-07,-3.279774E-07,-3.602629E-07,-3.432442E-07,-3.101731E-07,-3.106777E-07,-3.077875E-07,-2.515266E-07,-2.716324E-07,-2.829708E-07,-2.712552E-07,-3.137160E-07,-3.013648E-07,-2.715870E-07,-2.567016E-07,-2.522858E-07,-2.044882E-07,-2.176660E-07,-2.351316E-07,-2.217890E-07,-2.673366E-07,-2.481551E-07,-2.128741E-07,-2.105660E-07,-2.064025E-07,-1.583573E-07,-1.702018E-07,-1.854662E-07,-1.795808E-07,-1.727030E-07,-2.152844E-07,-1.949751E-07,-1.683622E-07,-1.635728E-07,-1.518929E-07,-1.176524E-07,-1.352912E-07,-1.438456E-07,-1.370873E-07,-1.770428E-07,-1.666996E-07,-1.437137E-07,-1.280380E-07,-1.254097E-07,-7.698703E-08,-8.887528E-08,-1.127303E-07,-1.061048E-07,-9.954999E-08,-1.394200E-07,-1.178231E-07,-9.575676E-08,-9.513309E-08,-9.700923E-08,-4.829641E-08,-6.977163E-08,-8.332560E-08,-7.565502E-08,-6.940003E-08,-1.109749E-07,-7.965935E-08,-7.048251E-08,-7.256974E-08,-2.189516E-08,-4.491847E-08,-5.792795E-08,-5.267491E-08,-8.578963E-08,-8.999892E-08,-6.910715E-08,-4.891971E-08,-4.996334E-08,-4.795953E-09,-9.800623E-09,-4.137087E-08,-3.528415E-08,-3.148711E-08,-6.885506E-08,-5.801331E-08,-2.810940E-08,-2.998735E-08,-3.307167E-08,+1.672317E-08,-6.881336E-09,-2.298073E-08,-1.509713E-08,-1.167718E-08,-5.625937E-08,-2.919237E-08,-1.259467E-08,-1.572228E-08,+2.881897E-08,+2.218799E-08,+5.546811E-09,+2.489909E-08,+3.073746E-08,+4.374773E-08,+1.045553E-07,+1.171487E-07,+1.333275E-07,+1.925549E-07,+2.839660E-07,+3.778138E-07,+6.219310E-07,+9.818317E-07,+1.521265E-06,+2.278791E-06,+3.358356E-06,+4.766202E-06,+6.572176E-06,+8.943498E-06,+1.176164E-05,+1.540627E-05,+1.985870E-05,+2.528661E-05,+3.166626E-05,+3.955172E-05,+4.888692E-05,+6.012289E-05,+7.383012E-05,+8.965204E-05,+1.092889E-04,+1.316348E-04,+1.582385E-04,+1.897523E-04,+2.269025E-04,+2.695382E-04,+3.194839E-04,+3.769088E-04,+4.431020E-04,+5.175397E-04,+6.053282E-04,+7.028421E-04,+8.145596E-04,+9.388500E-04,+1.076992E-03,+1.234904E-03,+1.415861E-03,+1.615935E-03,+1.835014E-03,+2.081876E-03,+2.350654E-03,+2.656946E-03,+2.983288E-03,+3.333431E-03,+3.720417E-03,+4.147862E-03,+4.584313E-03,+5.078487E-03,+5.594200E-03,+6.125263E-03,+6.706783E-03,+7.305480E-03,+7.930640E-03,+8.608914E-03,+9.302795E-03,+1.002572E-02,+1.078689E-02,+1.155413E-02,+1.236147E-02,+1.319274E-02,+1.401842E-02,+1.488659E-02,+1.576316E-02,+1.666857E-02,+1.759638E-02,+1.854674E-02,+1.952210E-02,+2.049219E-02,+2.146895E-02,+2.248172E-02,+2.350456E-02,+2.457303E-02,+2.559946E-02,+2.665722E-02,+2.774832E-02,+2.885042E-02,+2.995380E-02,+3.106694E-02,+3.218925E-02,+3.336091E-02,+3.443879E-02,+3.559057E-02,+3.678086E-02,+3.794563E-02,+3.909955E-02,+4.026622E-02,+4.147764E-02,+4.266360E-02,+4.385263E-02,+4.508207E-02,+4.625957E-02,+4.747749E-02,+4.875504E-02,+5.003934E-02,+5.125560E-02,+5.244925E-02,+5.368674E-02,+5.495569E-02,+5.625145E-02,+5.747259E-02,+5.873607E-02,+5.997975E-02,+6.126141E-02,+6.246920E-02,+6.377701E-02,+6.504284E-02,+6.627941E-02,+6.750420E-02};
// 
// int I9len=I_9.length;
// for(int i=0;i<I9len;i++)
// {I_9[i]=I_9[i]*(1+0.01*Math.random());}
// 
// double[] V_10={-3.013806E+00,-2.984209E+00,-2.954431E+00,-2.925587E+00,-2.895118E+00,-2.865851E+00,-2.836371E+00,-2.806851E+00,-2.777338E+00,-2.747195E+00,-2.717497E+00,-2.688474E+00,-2.658394E+00,-2.628708E+00,-2.599242E+00,-2.569795E+00,-2.540246E+00,-2.510099E+00,-2.481501E+00,-2.451403E+00,-2.421439E+00,-2.392306E+00,-2.362123E+00,-2.332325E+00,-2.303294E+00,-2.273414E+00,-2.243906E+00,-2.213812E+00,-2.184069E+00,-2.155035E+00,-2.125910E+00,-2.095697E+00,-2.065971E+00,-2.036705E+00,-2.006644E+00,-1.977363E+00,-1.947476E+00,-1.918274E+00,-1.888017E+00,-1.858031E+00,-1.828679E+00,-1.799911E+00,-1.770582E+00,-1.740396E+00,-1.710721E+00,-1.681213E+00,-1.651303E+00,-1.621660E+00,-1.591910E+00,-1.562325E+00,-1.532200E+00,-1.502949E+00,-1.473491E+00,-1.444043E+00,-1.414855E+00,-1.384813E+00,-1.355401E+00,-1.325741E+00,-1.296069E+00,-1.266304E+00,-1.236688E+00,-1.206841E+00,-1.177643E+00,-1.148000E+00,-1.118513E+00,-1.088943E+00,-1.059263E+00,-1.030066E+00,-1.000235E+00,-9.699778E-01,-9.409158E-01,-9.110630E-01,-8.808757E-01,-8.518612E-01,-8.218616E-01,-7.925916E-01,-7.632168E-01,-7.335190E-01,-7.035159E-01,-6.741370E-01,-6.443508E-01,-6.148112E-01,-5.849584E-01,-5.555028E-01,-5.260267E-01,-4.962903E-01,-4.672555E-01,-4.373065E-01,-4.077635E-01,-3.782273E-01,-3.481103E-01,-3.187530E-01,-2.885661E-01,-2.591901E-01,-2.292496E-01,-2.001278E-01,-1.699281E-01,-1.410636E-01,-1.109534E-01,+9.951832E-02,+1.294656E-01,+1.592535E-01,+1.886953E-01,+2.186488E-01,+2.481018E-01,+2.777153E-01,+3.077331E-01,+3.372072E-01,+3.673141E-01,+3.970910E-01,+4.262888E-01,+4.553941E-01,+4.851851E-01,+5.148001E-01,+5.444004E-01,+5.742803E-01,+6.038831E-01,+6.335102E-01,+6.631074E-01,+6.920495E-01,+7.225268E-01,+7.519347E-01,+7.813138E-01,+8.101664E-01,+8.407147E-01,+8.697743E-01,+8.993414E-01,+9.292555E-01,+9.590054E-01,+9.889033E-01,+1.018651E+00,+1.048294E+00,+1.078152E+00,+1.107600E+00,+1.136995E+00,+1.166163E+00,+1.195977E+00,+1.225551E+00,+1.255592E+00,+1.285399E+00,+1.314729E+00,+1.344788E+00,+1.374051E+00,+1.403397E+00,+1.432919E+00,+1.462303E+00,+1.491493E+00,+1.521720E+00,+1.550991E+00,+1.580939E+00,+1.610514E+00,+1.639947E+00,+1.670012E+00,+1.699687E+00,+1.729313E+00,+1.759273E+00,+1.788804E+00,+1.817082E+00,+1.847625E+00,+1.877256E+00,+1.906871E+00,+1.936692E+00,+1.966307E+00,+1.995826E+00,+2.025372E+00,+2.055307E+00,+2.084699E+00,+2.114314E+00,+2.144375E+00,+2.173101E+00,+2.202917E+00,+2.233310E+00,+2.262137E+00,+2.291887E+00,+2.321579E+00,+2.351116E+00,+2.380249E+00,+2.410926E+00,+2.440292E+00,+2.470206E+00,+2.499154E+00,+2.528883E+00,+2.558381E+00,+2.588551E+00,+2.618113E+00,+2.647645E+00,+2.677295E+00,+2.707005E+00,+2.735526E+00,+2.766447E+00,+2.796294E+00,+2.825641E+00,+2.854743E+00,+2.884499E+00,+2.914678E+00,+2.944293E+00,+2.973100E+00,+3.003272E+00};
// double[] I_10={-2.544859E-05,-2.513161E-05,-2.478015E-05,-2.452754E-05,-2.422874E-05,-2.398387E-05,-2.370386E-05,-2.350861E-05,-2.320886E-05,-2.283892E-05,-2.259353E-05,-2.232317E-05,-2.207361E-05,-2.177395E-05,-2.153898E-05,-2.128204E-05,-2.097782E-05,-2.070379E-05,-2.040709E-05,-2.019617E-05,-1.992617E-05,-1.969271E-05,-1.943215E-05,-1.917924E-05,-1.891605E-05,-1.860851E-05,-1.834190E-05,-1.816048E-05,-1.786814E-05,-1.763009E-05,-1.736248E-05,-1.710209E-05,-1.687773E-05,-1.657977E-05,-1.634580E-05,-1.609011E-05,-1.588654E-05,-1.559729E-05,-1.534556E-05,-1.510667E-05,-1.485985E-05,-1.457297E-05,-1.432258E-05,-1.412972E-05,-1.386338E-05,-1.359411E-05,-1.332680E-05,-1.309408E-05,-1.283364E-05,-1.259754E-05,-1.241810E-05,-1.218227E-05,-1.191857E-05,-1.164369E-05,-1.138975E-05,-1.117986E-05,-1.094157E-05,-1.068968E-05,-1.044672E-05,-1.024998E-05,-9.985188E-06,-9.686340E-06,-9.490469E-06,-9.291366E-06,-9.063562E-06,-8.775822E-06,-8.547305E-06,-8.291081E-06,-8.049059E-06,-7.830185E-06,-7.609830E-06,-7.350028E-06,-7.112786E-06,-6.829240E-06,-6.611390E-06,-6.447137E-06,-6.190573E-06,-5.889925E-06,-5.678348E-06,-5.467217E-06,-5.222712E-06,-5.031150E-06,-4.775645E-06,-4.507256E-06,-4.296969E-06,-4.092038E-06,-3.824889E-06,-3.586464E-06,-3.355707E-06,-3.089593E-06,-2.884861E-06,-2.643247E-06,-2.407387E-06,-2.169497E-06,-1.931520E-06,-1.704298E-06,-1.483263E-06,-1.208717E-06,-9.674218E-07,+9.607314E-07,+1.245689E-06,+1.649596E-06,+2.102801E-06,+2.551960E-06,+3.111659E-06,+3.871746E-06,+4.784711E-06,+6.148276E-06,+7.807906E-06,+9.981874E-06,+1.283663E-05,+1.647978E-05,+2.120766E-05,+2.729959E-05,+3.486173E-05,+4.416261E-05,+5.567086E-05,+6.909842E-05,+8.470073E-05,+1.039618E-04,+1.249510E-04,+1.477092E-04,+1.718331E-04,+1.989504E-04,+2.283436E-04,+2.591930E-04,+2.923756E-04,+3.273626E-04,+3.640525E-04,+4.030319E-04,+4.433940E-04,+4.859392E-04,+5.294950E-04,+5.740050E-04,+6.222149E-04,+6.725353E-04,+7.235426E-04,+7.784057E-04,+8.352342E-04,+8.952789E-04,+9.558441E-04,+1.021234E-03,+1.073188E-03,+1.143582E-03,+1.219362E-03,+1.297586E-03,+1.383010E-03,+1.474836E-03,+1.574307E-03,+1.673163E-03,+1.783162E-03,+1.900717E-03,+2.030434E-03,+2.162044E-03,+2.301037E-03,+2.449126E-03,+2.609378E-03,+2.749396E-03,+2.889432E-03,+3.022146E-03,+3.122712E-03,+3.231643E-03,+3.319698E-03,+3.399136E-03,+3.475701E-03,+3.548947E-03,+3.621675E-03,+3.685931E-03,+3.755972E-03,+3.825973E-03,+3.896605E-03,+3.971946E-03,+4.042599E-03,+4.103065E-03,+4.168232E-03,+4.234119E-03,+4.294650E-03,+4.360063E-03,+4.414807E-03,+4.475111E-03,+4.531019E-03,+4.582578E-03,+4.640188E-03,+4.697041E-03,+4.747082E-03,+4.805144E-03,+4.859475E-03,+4.909314E-03,+4.962286E-03,+5.015119E-03,+5.063262E-03,+5.105816E-03,+5.153987E-03,+5.198221E-03,+5.238563E-03,+5.287114E-03,+5.333162E-03,+5.378878E-03};
// 
// int I10len=I_10.length;
// for(int i=0;i<I10len;i++)
// {I_10[i]=I_10[i]*(1+0.01*Math.random());}
// 
// // int I10len=I_10.length;
// // for(int i=0;i<I10len;i++)
// // {I_10[i]=I_10[i]*(1+2.0*(Math.random()-0.5)*0.01);}
// 
// 
// double[] V_11={-3.014349E+00,-2.984562E+00,-2.954915E+00,-2.925135E+00,-2.895285E+00,-2.865607E+00,-2.836216E+00,-2.806977E+00,-2.777393E+00,-2.747519E+00,-2.717802E+00,-2.688311E+00,-2.658382E+00,-2.628566E+00,-2.599559E+00,-2.569530E+00,-2.539812E+00,-2.510248E+00,-2.481051E+00,-2.451554E+00,-2.422009E+00,-2.391908E+00,-2.362361E+00,-2.332346E+00,-2.303384E+00,-2.273399E+00,-2.243450E+00,-2.214360E+00,-2.184511E+00,-2.154773E+00,-2.125226E+00,-2.096169E+00,-2.066263E+00,-2.036691E+00,-2.006757E+00,-1.976809E+00,-1.947542E+00,-1.917927E+00,-1.887967E+00,-1.858457E+00,-1.828763E+00,-1.799458E+00,-1.770320E+00,-1.740396E+00,-1.710448E+00,-1.682067E+00,-1.651659E+00,-1.621660E+00,-1.591940E+00,-1.562648E+00,-1.532617E+00,-1.503003E+00,-1.473078E+00,-1.443542E+00,-1.414307E+00,-1.384776E+00,-1.355189E+00,-1.325565E+00,-1.295942E+00,-1.266169E+00,-1.236730E+00,-1.207373E+00,-1.177433E+00,-1.148188E+00,-1.117763E+00,-1.088779E+00,-1.059625E+00,-1.030052E+00,-1.000168E+00,-9.696195E-01,-9.404152E-01,-9.106741E-01,-8.809236E-01,-8.525866E-01,-8.222566E-01,-7.923253E-01,-7.628831E-01,-7.335338E-01,-7.036543E-01,-6.745677E-01,-6.442631E-01,-6.147237E-01,-5.854156E-01,-5.557493E-01,-5.262006E-01,-4.961702E-01,-4.668258E-01,-4.374586E-01,-4.078305E-01,-3.782095E-01,-3.486084E-01,-3.184129E-01,-2.881335E-01,-2.593413E-01,-2.296543E-01,-2.002126E-01,-1.701784E-01,-1.411470E-01,-1.108610E-01,+9.951899E-02,+1.296350E-01,+1.594118E-01,+1.889392E-01,+2.189766E-01,+2.482502E-01,+2.778728E-01,+3.073911E-01,+3.378565E-01,+3.673141E-01,+3.972498E-01,+4.266887E-01,+4.554683E-01,+4.856628E-01,+5.150330E-01,+5.449917E-01,+5.742007E-01,+6.038912E-01,+6.333970E-01,+6.631685E-01,+6.930553E-01,+7.221398E-01,+7.520182E-01,+7.810426E-01,+8.106669E-01,+8.401859E-01,+8.695065E-01,+8.997827E-01,+9.298206E-01,+9.593455E-01,+9.887534E-01,+1.018714E+00,+1.048752E+00,+1.077691E+00,+1.107556E+00,+1.137018E+00,+1.166608E+00,+1.196695E+00,+1.225785E+00,+1.254899E+00,+1.284971E+00,+1.314852E+00,+1.344743E+00,+1.374033E+00,+1.403286E+00,+1.433095E+00,+1.461920E+00,+1.491941E+00,+1.521221E+00,+1.551366E+00,+1.580845E+00,+1.611229E+00,+1.640570E+00,+1.670452E+00,+1.699069E+00,+1.729528E+00,+1.759178E+00,+1.788923E+00,+1.818061E+00,+1.848596E+00,+1.877481E+00,+1.907134E+00,+1.936812E+00,+1.966562E+00,+1.995899E+00,+2.025624E+00,+2.055155E+00,+2.084824E+00,+2.114729E+00,+2.144190E+00,+2.172984E+00,+2.202715E+00,+2.232569E+00,+2.262440E+00,+2.291992E+00,+2.321752E+00,+2.351120E+00,+2.380980E+00,+2.410145E+00,+2.440275E+00,+2.469522E+00,+2.499269E+00,+2.528869E+00,+2.558898E+00,+2.588109E+00,+2.617525E+00,+2.647651E+00,+2.677467E+00,+2.706895E+00,+2.736407E+00,+2.766762E+00,+2.795966E+00,+2.825834E+00,+2.855115E+00,+2.884543E+00,+2.914045E+00,+2.943896E+00,+2.973197E+00,+3.003191E+00};
// double[] I_11={-2.556504E-05,-2.521436E-05,-2.495532E-05,-2.467796E-05,-2.440559E-05,-2.415590E-05,-2.381936E-05,-2.352022E-05,-2.325089E-05,-2.297163E-05,-2.268242E-05,-2.243652E-05,-2.214888E-05,-2.187789E-05,-2.158722E-05,-2.133559E-05,-2.105610E-05,-2.079847E-05,-2.051831E-05,-2.027987E-05,-2.003464E-05,-1.975433E-05,-1.950102E-05,-1.922252E-05,-1.895337E-05,-1.872976E-05,-1.847533E-05,-1.819538E-05,-1.792021E-05,-1.765571E-05,-1.739920E-05,-1.712481E-05,-1.690784E-05,-1.661887E-05,-1.636830E-05,-1.613055E-05,-1.586955E-05,-1.559844E-05,-1.537643E-05,-1.508729E-05,-1.481563E-05,-1.459437E-05,-1.435101E-05,-1.409385E-05,-1.384692E-05,-1.362712E-05,-1.337119E-05,-1.313037E-05,-1.288451E-05,-1.263265E-05,-1.239808E-05,-1.214615E-05,-1.189123E-05,-1.166455E-05,-1.142357E-05,-1.121460E-05,-1.098375E-05,-1.072924E-05,-1.048266E-05,-1.026708E-05,-1.003011E-05,-9.765727E-06,-9.520195E-06,-9.277810E-06,-9.001331E-06,-8.759155E-06,-8.552623E-06,-8.325422E-06,-8.079465E-06,-7.843214E-06,-7.647412E-06,-7.392752E-06,-7.148327E-06,-6.889885E-06,-6.672608E-06,-6.421981E-06,-6.138727E-06,-5.927041E-06,-5.718745E-06,-5.489769E-06,-5.249664E-06,-5.017435E-06,-4.818834E-06,-4.562575E-06,-4.311250E-06,-4.075010E-06,-3.838592E-06,-3.562402E-06,-3.332215E-06,-3.112929E-06,-2.901317E-06,-2.659085E-06,-2.420438E-06,-2.223587E-06,-1.975211E-06,-1.715405E-06,-1.466454E-06,-1.220104E-06,-9.754768E-07,+9.938135E-07,+1.300938E-06,+1.636494E-06,+2.043004E-06,+2.496792E-06,+2.975958E-06,+3.654432E-06,+4.537168E-06,+5.635905E-06,+7.148068E-06,+9.128696E-06,+1.163446E-05,+1.504172E-05,+1.954747E-05,+2.539712E-05,+3.287719E-05,+4.199098E-05,+5.310305E-05,+6.624476E-05,+8.149882E-05,+9.870949E-05,+1.188357E-04,+1.400597E-04,+1.630446E-04,+1.885856E-04,+2.156814E-04,+2.446097E-04,+2.755366E-04,+3.083482E-04,+3.427784E-04,+3.787594E-04,+4.172082E-04,+4.564849E-04,+4.977535E-04,+5.404516E-04,+5.863524E-04,+6.330425E-04,+6.806189E-04,+7.312750E-04,+7.836966E-04,+8.398439E-04,+8.946366E-04,+9.547927E-04,+1.016598E-03,+1.074988E-03,+1.145375E-03,+1.213665E-03,+1.291945E-03,+1.371318E-03,+1.459145E-03,+1.550980E-03,+1.646807E-03,+1.754727E-03,+1.870165E-03,+1.988698E-03,+2.119387E-03,+2.254257E-03,+2.395338E-03,+2.536225E-03,+2.674829E-03,+2.813635E-03,+2.955136E-03,+3.068093E-03,+3.181711E-03,+3.268216E-03,+3.352014E-03,+3.421212E-03,+3.498871E-03,+3.573543E-03,+3.648018E-03,+3.710815E-03,+3.781660E-03,+3.847394E-03,+3.915203E-03,+3.984001E-03,+4.043671E-03,+4.104805E-03,+4.163629E-03,+4.225636E-03,+4.282265E-03,+4.338453E-03,+4.399143E-03,+4.447211E-03,+4.497947E-03,+4.551755E-03,+4.603066E-03,+4.664212E-03,+4.712658E-03,+4.759965E-03,+4.810192E-03,+4.862125E-03,+4.902201E-03,+4.952050E-03,+4.999142E-03,+5.044948E-03,+5.090988E-03,+5.135696E-03,+5.184533E-03,+5.226451E-03};
// 
// double[] V_12={-3.014038E+00,-2.984054E+00,-2.954800E+00,-2.925022E+00,-2.895026E+00,-2.865376E+00,-2.836014E+00,-2.806845E+00,-2.776789E+00,-2.747113E+00,-2.717064E+00,-2.688290E+00,-2.658311E+00,-2.628574E+00,-2.598598E+00,-2.568885E+00,-2.539556E+00,-2.509712E+00,-2.481235E+00,-2.450238E+00,-2.420993E+00,-2.391914E+00,-2.362171E+00,-2.332658E+00,-2.302880E+00,-2.272957E+00,-2.243701E+00,-2.213779E+00,-2.183574E+00,-2.154737E+00,-2.124861E+00,-2.095375E+00,-2.065846E+00,-2.035803E+00,-2.006498E+00,-1.977347E+00,-1.947051E+00,-1.917734E+00,-1.888732E+00,-1.858286E+00,-1.828934E+00,-1.799015E+00,-1.769832E+00,-1.740363E+00,-1.710906E+00,-1.680888E+00,-1.651448E+00,-1.621910E+00,-1.591718E+00,-1.562012E+00,-1.532588E+00,-1.502474E+00,-1.473499E+00,-1.443878E+00,-1.414655E+00,-1.384738E+00,-1.355674E+00,-1.325524E+00,-1.295372E+00,-1.265841E+00,-1.235871E+00,-1.206635E+00,-1.177155E+00,-1.147762E+00,-1.118021E+00,-1.088205E+00,-1.058670E+00,-1.029350E+00,-1.000191E+00,-9.692844E-01,-9.397301E-01,-9.107552E-01,-8.808584E-01,-8.516346E-01,-8.216952E-01,-7.917343E-01,-7.631975E-01,-7.338472E-01,-7.038383E-01,-6.738051E-01,-6.444396E-01,-6.138523E-01,-5.847286E-01,-5.553670E-01,-5.257376E-01,-4.961301E-01,-4.660375E-01,-4.370808E-01,-4.075611E-01,-3.776836E-01,-3.488282E-01,-3.179722E-01,-2.882712E-01,-2.585853E-01,-2.293871E-01,-1.998601E-01,-1.698333E-01,-1.400531E-01,-1.104387E-01,+9.984797E-02,+1.296202E-01,+1.599015E-01,+1.901802E-01,+2.194565E-01,+2.485672E-01,+2.784361E-01,+3.083859E-01,+3.371483E-01,+3.676132E-01,+3.976428E-01,+4.271749E-01,+4.561884E-01,+4.859474E-01,+5.153424E-01,+5.456260E-01,+5.748101E-01,+6.044732E-01,+6.342056E-01,+6.637211E-01,+6.933003E-01,+7.235335E-01,+7.527082E-01,+7.823404E-01,+8.116858E-01,+8.412364E-01,+8.704798E-01,+9.001742E-01,+9.299458E-01,+9.602562E-01,+9.892641E-01,+1.019584E+00,+1.049349E+00,+1.078939E+00,+1.108519E+00,+1.137495E+00,+1.167137E+00,+1.196292E+00,+1.226396E+00,+1.255999E+00,+1.286262E+00,+1.315514E+00,+1.344968E+00,+1.374265E+00,+1.404461E+00,+1.434436E+00,+1.463720E+00,+1.492631E+00,+1.522984E+00,+1.553097E+00,+1.581937E+00,+1.611495E+00,+1.641683E+00,+1.670784E+00,+1.700050E+00,+1.729912E+00,+1.760835E+00,+1.788968E+00,+1.818032E+00,+1.847934E+00,+1.877600E+00,+1.907532E+00,+1.936648E+00,+1.967114E+00,+1.996252E+00,+2.025766E+00,+2.055920E+00,+2.085058E+00,+2.115254E+00,+2.144771E+00,+2.173363E+00,+2.203564E+00,+2.233071E+00,+2.263092E+00,+2.293151E+00,+2.322685E+00,+2.352193E+00,+2.381943E+00,+2.410766E+00,+2.441813E+00,+2.470157E+00,+2.499518E+00,+2.530132E+00,+2.559092E+00,+2.589071E+00,+2.618117E+00,+2.647592E+00,+2.678628E+00,+2.707018E+00,+2.737576E+00,+2.767326E+00,+2.796733E+00,+2.827115E+00,+2.855603E+00,+2.884792E+00,+2.914860E+00,+2.944437E+00,+2.973794E+00,+3.003470E+00};
// double[] I_12={-2.385488E-06,-2.315574E-06,-2.235827E-06,-2.187573E-06,-2.137403E-06,-2.130868E-06,-2.071769E-06,-2.041323E-06,-2.007623E-06,-1.970147E-06,-1.910362E-06,-1.923675E-06,-1.874698E-06,-1.854674E-06,-1.791275E-06,-1.757564E-06,-1.718867E-06,-1.724426E-06,-1.712741E-06,-1.696998E-06,-1.630484E-06,-1.636344E-06,-1.610912E-06,-1.575306E-06,-1.576390E-06,-1.536060E-06,-1.490629E-06,-1.460519E-06,-1.445178E-06,-1.435796E-06,-1.425068E-06,-1.353277E-06,-1.342433E-06,-1.330496E-06,-1.336901E-06,-1.266892E-06,-1.235070E-06,-1.227205E-06,-1.215543E-06,-1.181886E-06,-1.139424E-06,-1.120349E-06,-1.100285E-06,-1.046228E-06,-1.069285E-06,-1.051525E-06,-1.003305E-06,-9.633870E-07,-9.334096E-07,-9.747766E-07,-9.447929E-07,-9.254080E-07,-8.828067E-07,-8.614888E-07,-8.528800E-07,-8.443891E-07,-8.151309E-07,-8.309652E-07,-7.826534E-07,-7.414793E-07,-7.246645E-07,-7.179672E-07,-7.122822E-07,-7.102611E-07,-6.905155E-07,-6.239704E-07,-6.240080E-07,-6.032626E-07,-5.673672E-07,-5.462860E-07,-5.199128E-07,-4.982087E-07,-4.650782E-07,-4.253585E-07,-4.069690E-07,-4.271878E-07,-4.007830E-07,-3.675909E-07,-3.297730E-07,-3.453272E-07,-3.224700E-07,-3.106381E-07,-2.929899E-07,-2.968269E-07,-2.924379E-07,-2.734638E-07,-2.165026E-07,-2.095319E-07,-2.079929E-07,-2.038974E-07,-1.645715E-07,-1.490599E-07,-1.346267E-07,-1.222441E-07,-9.329799E-08,-6.656442E-08,-5.484548E-08,-3.453365E-08,-7.194365E-08,+3.549733E-07,+6.704551E-07,+1.123690E-06,+1.948179E-06,+3.012126E-06,+4.352136E-06,+6.016072E-06,+8.038011E-06,+1.044100E-05,+1.325226E-05,+1.644333E-05,+2.001532E-05,+2.402976E-05,+2.854420E-05,+3.356024E-05,+3.909972E-05,+4.521781E-05,+5.177976E-05,+5.881104E-05,+6.632123E-05,+7.426996E-05,+8.257866E-05,+9.112949E-05,+1.000764E-04,+1.101711E-04,+1.201671E-04,+1.298922E-04,+1.406063E-04,+1.511944E-04,+1.625562E-04,+1.739512E-04,+1.857239E-04,+1.978331E-04,+2.101908E-04,+2.232179E-04,+2.365909E-04,+2.504274E-04,+2.647324E-04,+2.796362E-04,+2.953271E-04,+3.120270E-04,+3.294124E-04,+3.471985E-04,+3.665792E-04,+3.886522E-04,+4.119542E-04,+4.336693E-04,+4.617155E-04,+4.900150E-04,+5.192894E-04,+5.564097E-04,+5.941344E-04,+6.359739E-04,+6.748197E-04,+7.156794E-04,+7.636923E-04,+8.133416E-04,+8.625843E-04,+9.184371E-04,+9.835056E-04,+1.025715E-03,+1.090227E-03,+1.155783E-03,+1.220657E-03,+1.290991E-03,+1.352749E-03,+1.427558E-03,+1.523174E-03,+1.612153E-03,+1.681483E-03,+1.765024E-03,+1.837791E-03,+1.914817E-03,+2.015145E-03,+2.188216E-03,+2.291781E-03,+2.443907E-03,+2.540881E-03,+2.628103E-03,+2.712328E-03,+2.855895E-03,+2.988228E-03,+2.987703E-03,+3.243490E-03,+3.425793E-03,+3.459306E-03,+3.635612E-03,+3.695125E-03,+4.224518E-03,+4.617094E-03,+4.775256E-03,+4.972212E-03,+5.178526E-03,+5.357082E-03,+5.106150E-03,+5.208277E-03,+5.350784E-03,+5.446646E-03,+5.530357E-03};
// 
// double[] V_13={-3.013567E+00,-2.984308E+00,-2.953859E+00,-2.924453E+00,-2.895852E+00,-2.864888E+00,-2.835994E+00,-2.806799E+00,-2.776881E+00,-2.747372E+00,-2.717880E+00,-2.688065E+00,-2.657804E+00,-2.628073E+00,-2.599160E+00,-2.569334E+00,-2.539507E+00,-2.509345E+00,-2.481051E+00,-2.450805E+00,-2.421612E+00,-2.391580E+00,-2.361905E+00,-2.331605E+00,-2.303334E+00,-2.272941E+00,-2.243596E+00,-2.213687E+00,-2.183976E+00,-2.155198E+00,-2.125166E+00,-2.095402E+00,-2.066139E+00,-2.036290E+00,-2.006248E+00,-1.977163E+00,-1.947442E+00,-1.917856E+00,-1.888552E+00,-1.858465E+00,-1.828461E+00,-1.798812E+00,-1.769832E+00,-1.740149E+00,-1.710405E+00,-1.681460E+00,-1.651204E+00,-1.621881E+00,-1.592321E+00,-1.562397E+00,-1.532535E+00,-1.502143E+00,-1.473269E+00,-1.444378E+00,-1.413923E+00,-1.384988E+00,-1.354802E+00,-1.325125E+00,-1.295636E+00,-1.265515E+00,-1.235938E+00,-1.206192E+00,-1.177215E+00,-1.147527E+00,-1.117802E+00,-1.088782E+00,-1.058927E+00,-1.029038E+00,-9.996206E-01,-9.705486E-01,-9.399285E-01,-9.106007E-01,-8.808287E-01,-8.514677E-01,-8.216897E-01,-7.921726E-01,-7.625970E-01,-7.336755E-01,-7.032686E-01,-6.733273E-01,-6.438731E-01,-6.145782E-01,-5.845696E-01,-5.551130E-01,-5.247684E-01,-4.958899E-01,-4.657778E-01,-4.370750E-01,-4.072247E-01,-3.781159E-01,-3.481469E-01,-3.181305E-01,-2.890142E-01,-2.585801E-01,-2.293037E-01,-1.990273E-01,-1.699944E-01,-1.402171E-01,-1.109399E-01,+1.004319E-01,+1.299600E-01,+1.594855E-01,+1.895129E-01,+2.192970E-01,+2.488208E-01,+2.780153E-01,+3.088030E-01,+3.381583E-01,+3.673431E-01,+3.973846E-01,+4.270599E-01,+4.559290E-01,+4.857904E-01,+5.160028E-01,+5.458542E-01,+5.742031E-01,+6.048557E-01,+6.345222E-01,+6.637078E-01,+6.933931E-01,+7.235432E-01,+7.529534E-01,+7.818452E-01,+8.116570E-01,+8.406850E-01,+8.705749E-01,+9.005200E-01,+9.302920E-01,+9.597428E-01,+9.896044E-01,+1.019049E+00,+1.048681E+00,+1.078529E+00,+1.108050E+00,+1.137578E+00,+1.167221E+00,+1.196900E+00,+1.226429E+00,+1.255865E+00,+1.285977E+00,+1.315431E+00,+1.344876E+00,+1.374830E+00,+1.404257E+00,+1.433775E+00,+1.463127E+00,+1.492808E+00,+1.522129E+00,+1.552304E+00,+1.581530E+00,+1.611248E+00,+1.641189E+00,+1.670833E+00,+1.700376E+00,+1.730830E+00,+1.760132E+00,+1.787658E+00,+1.819118E+00,+1.848397E+00,+1.877208E+00,+1.907365E+00,+1.937630E+00,+1.967057E+00,+1.996125E+00,+2.025629E+00,+2.056101E+00,+2.085101E+00,+2.115521E+00,+2.144892E+00,+2.173657E+00,+2.203367E+00,+2.233411E+00,+2.263319E+00,+2.292110E+00,+2.321721E+00,+2.352594E+00,+2.381654E+00,+2.411700E+00,+2.441579E+00,+2.470825E+00,+2.500203E+00,+2.530265E+00,+2.559092E+00,+2.588821E+00,+2.618051E+00,+2.649248E+00,+2.677442E+00,+2.708131E+00,+2.737547E+00,+2.766649E+00,+2.797040E+00,+2.826337E+00,+2.856238E+00,+2.885022E+00,+2.914639E+00,+2.944540E+00,+2.974485E+00,+3.004114E+00};
// double[] I_13={-3.337528E-06,-3.239506E-06,-3.180411E-06,-3.115599E-06,-3.083401E-06,-3.028246E-06,-2.995732E-06,-2.903060E-06,-2.883983E-06,-2.840252E-06,-2.823441E-06,-2.779861E-06,-2.693296E-06,-2.668237E-06,-2.611672E-06,-2.603491E-06,-2.577209E-06,-2.491661E-06,-2.441138E-06,-2.382405E-06,-2.371982E-06,-2.287896E-06,-2.300597E-06,-2.250652E-06,-2.214041E-06,-2.132645E-06,-2.140500E-06,-2.110251E-06,-2.072617E-06,-2.021734E-06,-1.974274E-06,-1.931080E-06,-1.930259E-06,-1.904288E-06,-1.856522E-06,-1.813547E-06,-1.797716E-06,-1.760245E-06,-1.726272E-06,-1.718288E-06,-1.683610E-06,-1.607378E-06,-1.583898E-06,-1.567068E-06,-1.537655E-06,-1.527061E-06,-1.487585E-06,-1.423025E-06,-1.389256E-06,-1.386772E-06,-1.326841E-06,-1.296786E-06,-1.256224E-06,-1.233177E-06,-1.162393E-06,-1.172744E-06,-1.144058E-06,-1.111041E-06,-1.063334E-06,-1.018243E-06,-1.016018E-06,-9.822621E-07,-9.384018E-07,-8.917796E-07,-8.609351E-07,-8.759133E-07,-8.442223E-07,-8.100846E-07,-7.684833E-07,-7.830704E-07,-7.111609E-07,-6.896025E-07,-6.613722E-07,-6.562007E-07,-6.472757E-07,-6.070793E-07,-5.490081E-07,-5.183940E-07,-4.821365E-07,-4.616613E-07,-3.983021E-07,-3.657191E-07,-3.884202E-07,-3.537960E-07,-3.128507E-07,-2.705916E-07,-2.417724E-07,-2.516100E-07,-2.290117E-07,-2.138319E-07,-1.935493E-07,-2.054836E-07,-1.892980E-07,-1.337943E-07,-1.206576E-07,-1.284168E-07,-1.160266E-07,-1.188217E-07,-7.794991E-08,+3.587270E-07,+6.579328E-07,+1.174871E-06,+1.963110E-06,+3.043507E-06,+4.394194E-06,+6.073879E-06,+8.125681E-06,+1.045088E-05,+1.334246E-05,+1.656061E-05,+2.015314E-05,+2.416690E-05,+2.877291E-05,+3.387175E-05,+3.947475E-05,+4.558216E-05,+5.223355E-05,+5.934580E-05,+6.688126E-05,+7.491614E-05,+8.338598E-05,+9.213232E-05,+1.010531E-04,+1.112045E-04,+1.210898E-04,+1.313203E-04,+1.416801E-04,+1.525560E-04,+1.636175E-04,+1.753578E-04,+1.869355E-04,+1.993012E-04,+2.116019E-04,+2.246379E-04,+2.378087E-04,+2.516160E-04,+2.656761E-04,+2.805238E-04,+2.956988E-04,+3.117392E-04,+3.277441E-04,+3.449982E-04,+3.635393E-04,+3.826235E-04,+4.032279E-04,+4.233817E-04,+4.476589E-04,+4.682792E-04,+4.919120E-04,+5.147398E-04,+5.456479E-04,+5.772770E-04,+6.157835E-04,+6.505777E-04,+6.904636E-04,+7.338860E-04,+7.847862E-04,+8.234455E-04,+8.854230E-04,+9.649000E-04,+1.037469E-03,+1.130632E-03,+1.194356E-03,+1.280590E-03,+1.371439E-03,+1.491922E-03,+1.529397E-03,+1.629663E-03,+1.703860E-03,+1.824741E-03,+1.965389E-03,+2.073715E-03,+2.241424E-03,+2.318426E-03,+2.433824E-03,+2.553371E-03,+2.686076E-03,+2.809715E-03,+2.977483E-03,+3.124940E-03,+3.319841E-03,+3.438638E-03,+3.556287E-03,+3.739093E-03,+3.916525E-03,+3.877237E-03,+4.310033E-03,+4.337748E-03,+4.464082E-03,+4.686930E-03,+4.927394E-03,+5.106643E-03,+5.305741E-03,+5.454992E-03,+5.559819E-03,+5.602893E-03,+5.676087E-03,+5.754313E-03}; 


//Xphoto++
// double[] V_14={+6.707279E-01,+6.956732E-01,+7.214094E-01,+7.468693E-01,+7.717001E-01,+7.974842E-01,+8.227630E-01,+8.477634E-01,+8.732323E-01,+8.990367E-01,+9.237983E-01,+9.497205E-01,+9.745001E-01,+9.999138E-01,+1.025548E+00,+1.051149E+00,+1.076451E+00,+1.101158E+00,+1.126818E+00,+1.152410E+00,+1.177683E+00};
// double[] I_14={+5.872143E-05,+7.257231E-05,+8.928285E-05,+1.098784E-04,+1.325953E-04,+1.597179E-04,+1.905476E-04,+2.268793E-04,+2.698813E-04,+3.190254E-04,+3.756418E-04,+4.400819E-04,+5.138126E-04,+5.979883E-04,+6.890984E-04,+7.990547E-04,+9.169353E-04,+1.046693E-03,+1.194215E-03,+1.362022E-03,+1.539063E-03};
// 
// int I14len=I_14.length;
// for(int i=0;i<I14len;i++)
// {I_14[i]=I_14[i]*(1+2.0*(Math.random()-0.5)*0.01);}

// //Xphoto2++
// double[] V_15={+6.198290E-01,+6.445014E-01,+6.706399E-01,+6.955617E-01,+7.210214E-01,+7.466392E-01,+7.719659E-01,+7.972340E-01,+8.227017E-01,+8.478033E-01,+8.732206E-01,+8.988457E-01,+9.247158E-01,+9.494319E-01,+9.750775E-01,+1.000254E+00,+1.025291E+00,+1.050898E+00,+1.076219E+00,+1.101902E+00,+1.126651E+00,+1.152387E+00,+1.177791E+00,+1.203390E+00,+1.228981E+00,+1.253879E+00,+1.279178E+00,+1.304245E+00};
// double[] I_15={+5.677512E-05,+6.579094E-05,+7.658517E-05,+8.926821E-05,+1.027355E-04,+1.195657E-04,+1.380614E-04,+1.599389E-04,+1.843801E-04,+2.124889E-04,+2.432698E-04,+2.786413E-04,+3.189544E-04,+3.610811E-04,+4.117054E-04,+4.674054E-04,+5.277263E-04,+5.969869E-04,+6.766552E-04,+7.641246E-04,+8.558444E-04,+9.646483E-04,+1.070688E-03,+1.194215E-03,+1.340711E-03,+1.491135E-03,+1.661947E-03,+1.845303E-03};
// 
//  int I15len=I_15.length;
//  for(int i=0;i<I15len;i++)
//  {I_15[i]=I_15[i]*(1+2.0*(Math.random()-0.5)*0.01);}

//X325-2++
// double[] V_16={+4.417549E-01,+4.670873E-01,+4.927036E-01,+5.183175E-01,+5.433409E-01,+5.689520E-01,+5.938961E-01,+6.199291E-01,+6.451113E-01,+6.705295E-01,+6.957613E-01,+7.209331E-01,+7.465940E-01,+7.721483E-01,+7.970052E-01,+8.223792E-01,+8.474868E-01,+8.732323E-01,+8.991914E-01,+9.238816E-01,+9.499963E-01,+9.744430E-01,+1.000545E+00,+1.025312E+00,+1.051232E+00,+1.076582E+00,+1.102052E+00,+1.127091E+00,+1.152136E+00,+1.177449E+00,+1.202648E+00,+1.228664E+00,+1.253688E+00,+1.278919E+00,+1.303999E+00,+1.330234E+00};
// double[] I_16={+4.707909E-05,+5.383181E-05,+6.085228E-05,+6.843476E-05,+7.658779E-05,+8.546499E-05,+9.506717E-05,+1.061645E-04,+1.179868E-04,+1.309322E-04,+1.450872E-04,+1.611068E-04,+1.791542E-04,+1.987091E-04,+2.205381E-04,+2.444574E-04,+2.712333E-04,+3.014024E-04,+3.355911E-04,+3.725681E-04,+4.136225E-04,+4.588941E-04,+5.091560E-04,+5.646430E-04,+6.265895E-04,+6.933681E-04,+7.678077E-04,+8.481083E-04,+9.354133E-04,+1.025222E-03,+1.129995E-03,+1.242469E-03,+1.364877E-03,+1.497869E-03,+1.638214E-03,+1.795207E-03};
// 
//  int I16len=I_16.length;
//  for(int i=0;i<I16len;i++)
//  {I_16[i]=I_16[i]*(1+2.0*(Math.random()-0.5)*0.01);}

//1f4tcnq
// double[] V_17={+6.042758E-01,+6.337094E-01,+6.634887E-01,+6.931013E-01,+7.234250E-01,+7.528372E-01,+7.814333E-01,+8.111346E-01,+8.411756E-01,+8.698359E-01,+9.002045E-01,+9.296162E-01,+9.587612E-01,+9.897144E-01,+1.019305E+00,+1.048724E+00,+1.078435E+00,+1.108208E+00,+1.137108E+00,+1.166796E+00,+1.196114E+00,+1.226336E+00,+1.255615E+00,+1.285822E+00,+1.315466E+00,+1.344921E+00,+1.374089E+00,+1.403868E+00,+1.433843E+00,+1.463392E+00};
// double[] I_17={+5.400027E-05,+6.289880E-05,+7.334287E-05,+8.680075E-05,+1.002689E-04,+1.173967E-04,+1.369219E-04,+1.583940E-04,+1.796466E-04,+2.050429E-04,+2.321205E-04,+2.573671E-04,+2.930628E-04,+3.251710E-04,+3.655659E-04,+4.066141E-04,+4.427898E-04,+4.978594E-04,+5.446015E-04,+5.958683E-04,+6.678312E-04,+7.535531E-04,+8.226059E-04,+8.853729E-04,+9.797971E-04,+1.082368E-03,+1.201421E-03,+1.355237E-03,+1.430047E-03,+1.619883E-03};
// 
//  int I17len=I_17.length;
//  for(int i=0;i<I17len;i++)
//  {I_17[i]=I_17[i]*(1+2.0*(Math.random()-0.5)*0.01);}


// double[] V_10={+6.038831E-01,+6.335102E-01,+6.631074E-01,+6.920495E-01,+7.225268E-01,+7.519347E-01,+7.813138E-01,+8.101664E-01,+8.407147E-01,+8.697743E-01,+8.993414E-01,+9.292555E-01,+9.590054E-01,+9.889033E-01,+1.018651E+00,+1.048294E+00,+1.078152E+00,+1.107600E+00,+1.136995E+00,+1.166163E+00,+1.195977E+00,+1.225551E+00,+1.255592E+00,+1.285399E+00,+1.314729E+00,+1.344788E+00,+1.374051E+00,+1.403397E+00,};
// double[] I_10={+5.567086E-05,+6.909842E-05,+8.470073E-05,+1.039618E-04,+1.249510E-04,+1.477092E-04,+1.718331E-04,+1.989504E-04,+2.283436E-04,+2.591930E-04,+2.923756E-04,+3.273626E-04,+3.640525E-04,+4.030319E-04,+4.433940E-04,+4.859392E-04,+5.294950E-04,+5.740050E-04,+6.222149E-04,+6.725353E-04,+7.235426E-04,+7.784057E-04,+8.352342E-04,+8.952789E-04,+9.558441E-04,+1.021234E-03,+1.073188E-03,+1.143582E-03,};
//  
//  int I10len=I_10.length;
//  for(int i=0;i<I10len;i++)
//  {I_10[i]=I_10[i]*(1+2.0*(Math.random()-0.5)*0.01);}

 
// //n6 V11 
//  double[] V_11={ +6.038912E-01,+6.333970E-01,+6.631685E-01,+6.930553E-01,+7.221398E-01,+7.520182E-01,+7.810426E-01,+8.106669E-01,+8.401859E-01,+8.695065E-01,+8.997827E-01,+9.298206E-01,+9.593455E-01,+9.887534E-01,+1.018714E+00,+1.048752E+00,+1.077691E+00,+1.107556E+00,+1.137018E+00,+1.166608E+00,+1.196695E+00,+1.225785E+00,+1.254899E+00,+1.284971E+00,+1.314852E+00,+1.344743E+00,+1.374033E+00,+1.403286E+00,+1.433095E+00, };
//  double[] I_11={ +5.310305E-05,+6.624476E-05,+8.149882E-05,+9.870949E-05,+1.188357E-04,+1.400597E-04,+1.630446E-04,+1.885856E-04,+2.156814E-04,+2.446097E-04,+2.755366E-04,+3.083482E-04,+3.427784E-04,+3.787594E-04,+4.172082E-04,+4.564849E-04,+4.977535E-04,+5.404516E-04,+5.863524E-04,+6.330425E-04,+6.806189E-04,+7.312750E-04,+7.836966E-04,+8.398439E-04,+8.946366E-04,+9.547927E-04,+1.016598E-03,+1.074988E-03,+1.145375E-03, };
//  
//  int I11len=I_11.length;
//  for(int i=0;i<I11len;i++)
//  {I_11[i]=I_11[i]*(1+2.0*(Math.random()-0.5)*0.01);}
//  
//  
//  double[] V_4={  +5.452126E-01,+5.746472E-01,+6.042270E-01,+6.339468E-01,+6.638836E-01,+6.940050E-01,+7.230136E-01,+7.526349E-01,+7.820225E-01,+8.115408E-01,+8.403910E-01,+8.709495E-01,+9.001150E-01,+9.304963E-01,+9.595184E-01,+9.894642E-01,+1.019160E+00,+1.049015E+00,+1.078787E+00,+1.108224E+00,+1.137700E+00,+1.167495E+00,+1.196685E+00,+1.226745E+00,+1.256374E+00,+1.285946E+00,+1.315338E+00,+1.344977E+00,+1.374636E+00,+1.403895E+00,+1.433721E+00,+1.463382E+00,+1.492778E+00,+1.522410E+00,+1.552002E+00,+1.581552E+00, };
//  double[] I_4={  +2.282994E-05,+3.265913E-05,+4.547259E-05,+6.128139E-05,+8.003513E-05,+1.012809E-04,+1.256682E-04,+1.525581E-04,+1.813834E-04,+2.126781E-04,+2.460281E-04,+2.810921E-04,+3.178513E-04,+3.556531E-04,+3.960776E-04,+4.367381E-04,+4.808895E-04,+5.246410E-04,+5.702397E-04,+6.183776E-04,+6.664080E-04,+7.175178E-04,+7.685868E-04,+8.218214E-04,+8.777060E-04,+9.333867E-04,+9.924220E-04,+1.046103E-03,+1.109118E-03,+1.172512E-03,+1.240784E-03,+1.310312E-03,+1.385549E-03,+1.457680E-03,+1.547039E-03,+1.637857E-03,};
// 
//  int I4len=I_4.length;
//  for(int i=0;i<I4len;i++)
//  {I_4[i]=I_4[i]*(1+2.0*(Math.random()-0.5)*0.01);}
//  
//  double[] V_6={ +6.708158E-01,+6.961643E-01,+7.212619E-01,+7.471930E-01,+7.718929E-01,+7.976779E-01,+8.222349E-01,+8.478033E-01,+8.731431E-01,+8.990367E-01,+9.242800E-01,+9.492651E-01,+9.747570E-01,+1.000108E+00,+1.025798E+00,+1.051225E+00,+1.076248E+00,+1.101915E+00,+1.126871E+00,+1.152553E+00,+1.178049E+00,+1.203732E+00,+1.228790E+00,+1.253630E+00,+1.279169E+00,+1.303951E+00,+1.330502E+00,+1.354849E+00,+1.380910E+00,       };
//  double[] I_6={ +6.192451E-05,+7.221141E-05,+8.410038E-05,+9.789787E-05,+1.134609E-04,+1.313295E-04,+1.512435E-04,+1.737522E-04,+1.994099E-04,+2.292802E-04,+2.617946E-04,+2.986169E-04,+3.396886E-04,+3.856295E-04,+4.366066E-04,+4.948464E-04,+5.579883E-04,+6.293258E-04,+7.076098E-04,+7.949182E-04,+8.893922E-04,+9.973100E-04,+1.106345E-03,+1.232109E-03,+1.372421E-03,+1.526239E-03,+1.697973E-03,+1.880507E-03,+2.079165E-03,       };
// 
//  int I6len=I_6.length;
//  for(int i=0;i<I6len;i++)
//  {I_6[i]=I_6[i]*(1+2.0*(Math.random()-0.5)*0.01);}
// 
 double[] V_7={ +6.700155E-01,+6.954783E-01,+7.211640E-01,+7.472914E-01,+7.719399E-01,+7.969999E-01,+8.223515E-01,+8.477977E-01,+8.728270E-01,+8.983089E-01,+9.238505E-01,+9.497205E-01,+9.748404E-01,+1.000358E+00,+1.025215E+00,+1.051385E+00,+1.076513E+00,+1.101824E+00,+1.126848E+00,+1.152357E+00,+1.177115E+00,+1.203457E+00,+1.228631E+00,+1.253562E+00,+1.279379E+00,+1.304227E+00,+1.330168E+00,+1.355665E+00,+1.380641E+00,+1.406026E+00,          };
 double[] I_7={ +3.982476E-05,+4.905299E-05,+6.027925E-05,+7.384914E-05,+8.967056E-05,+1.090735E-04,+1.314895E-04,+1.580112E-04,+1.896214E-04,+2.272755E-04,+2.701612E-04,+3.209290E-04,+3.786755E-04,+4.451647E-04,+5.208108E-04,+6.104453E-04,+7.088086E-04,+8.227184E-04,+9.489789E-04,+1.086878E-03,+1.246848E-03,+1.427458E-03,+1.627916E-03,+1.850858E-03,+2.097613E-03,+2.365267E-03,+2.669387E-03,+2.994048E-03,+3.353141E-03,+3.742937E-03,         };
// 
  int I7len=I_7.length;
  for(int i=0;i<I7len;i++)
  {I_7[i]=I_7[i]*(1+2.0*(Math.random()-0.5)*0.01);}





double q=1.602176565e-19;// electronic charge
double kb=1.3806505e-23;//Boltzmann
double T=300.0;
double beta=(q/(kb*T));

int m=V_7.length;
double sum=0.0;
   for(int i=0;i<m;i++)
   {sum+=Math.pow((I_7[i] - (  x[0]*(  Math.exp(  beta  *  (V_7[i]-x[1]*I_7[i])  /  x[2]  ) - 1.0  ) + (  (V_7[i]-x[1]*I_7[i]) / x[3]   ) ) ),2.0);}
   //+ (  (V_1[i]-x[1]*I_1[i]) / x[3]   ) )
return Math.sqrt(sum/(double)m);
}

}
 
class parf1 extends f_xj
{

public double func(double x[]) 
{
int M=301;	
double[] X1 =new double[M];
double[] X2 =new double[M];
double[] X3 =new double[M];

double[] XX1 =new double[M];
double[] XX2 =new double[M];
double[] XX3 =new double[M];

double[] XX1d =new double[M];
double[] XX2d =new double[M];
double[] XX3d =new double[M];


X1[0]=2.0;
X2[0]=3.0;
X3[0]=2.0;

double beta=0.001;	

for(int i=0;i<M-1;i++)
{
X1[i+1]=((10.0*(X2[i]-X1[i]))*beta)+X1[i];
X2[i+1]=((28.0*X1[i]-X2[i]-X1[i]*X3[i])*beta)+X2[i];
X3[i+1]=((X1[i]*X2[i]-(8.0/3.0)*X3[i])*beta)+X3[i];	
}

XX1[0]=2.0;
XX2[0]=3.0;
XX3[0]=2.0;

for(int i=0;i<M-1;i++)
{
XX1[i+1]=((x[0]*(XX2[i]-XX1[i]))*beta)+XX1[i];
XX2[i+1]=((x[1]*XX1[i]-XX2[i]-XX1[i]*XX3[i])*beta)+XX2[i];
XX3[i+1]=((XX1[i]*XX2[i]-x[2]*XX3[i])*beta)+XX3[i];	
}

double s1=0.0;
double s2=0.0;
double s3=0.0;

// for(int i=0;i<M;i++)
// {s1+=Math.abs(X1[i]-XX1[i]);}
// for(int i=0;i<M;i++)
// {s2+=Math.abs(X2[i]-XX2[i]);}
// for(int i=0;i<M;i++)
// {s3+=Math.abs(X3[i]-XX3[i]);}

double[] Xdata=new double[3*M];
double[] Xcalc=new double[3*M];

for(int i=0;i<M;i++)
{Xdata[i]=X1[i];Xcalc[i]=XX1[i];}
int t1=0;
for(int i=M;i<2*M;i++)
{Xdata[i]=X2[t1];Xcalc[i]=XX2[t1];t1++;}
int t2=0;
for(int i=2*M;i<3*M;i++)
{Xdata[i]=X3[t2];Xcalc[i]=XX3[t2];t2++;}

for(int i=0;i<3*M;i++)
{s1+=(Xdata[i]-Xcalc[i])*(Xdata[i]-Xcalc[i]);}

double error=Math.sqrt(s1)/(double)(3*M);
return error;
}	
	
}



class parf2 extends f_xj
{
// Chen
public double func(double x[]) 
{
int M=301;	
double[] X1 =new double[M];
double[] X2 =new double[M];
double[] X3 =new double[M];

double[] XX1 =new double[M];
double[] XX2 =new double[M];
double[] XX3 =new double[M];

double[] XX1d =new double[M];
double[] XX2d =new double[M];
double[] XX3d =new double[M];


X1[0]=1.0;
X2[0]=1.0;
X3[0]=1.0;

double beta=0.001;	

double d1=35.0;
double d2=3.0;
double d3=28.0;

for(int i=0;i<M-1;i++)
{
X1[i+1]=((d1*(X2[i]-X1[i]))*beta)+X1[i];
X2[i+1]=(((d3-d1)*X1[i]-d3*X2[i]-X1[i]*X3[i])*beta)+X2[i];
X3[i+1]=((X1[i]*X2[i]-(d2)*X3[i])*beta)+X3[i];	
}

XX1[0]=1.0;
XX2[0]=1.0;
XX3[0]=1.0;

for(int i=0;i<M-1;i++)
{
XX1[i+1]=((x[0]*(XX2[i]-XX1[i]))*beta)+XX1[i];
XX2[i+1]=(((x[2]-x[0])*XX1[i]-x[2]*XX2[i]-XX1[i]*XX3[i])*beta)+XX2[i];
XX3[i+1]=((XX1[i]*XX2[i]-x[1]*XX3[i])*beta)+XX3[i];	
}

double s1=0.0;
double s2=0.0;
double s3=0.0;

double[] Xdata=new double[3*M];
double[] Xcalc=new double[3*M];

for(int i=0;i<M;i++)
{Xdata[i]=X1[i];Xcalc[i]=XX1[i];}
int t1=0;
for(int i=M;i<2*M;i++)
{Xdata[i]=X2[t1];Xcalc[i]=XX2[t1];t1++;}
int t2=0;
for(int i=2*M;i<3*M;i++)
{Xdata[i]=X3[t2];Xcalc[i]=XX3[t2];t2++;}

for(int i=0;i<3*M;i++)
{s1+=(Xdata[i]-Xcalc[i])*(Xdata[i]-Xcalc[i]);}

double error=Math.sqrt(s1)/(double)(3*M);
return error;
}	
	
}



class parf3 extends f_xj
{
// Rossler
public double func(double x[]) 
{
int M=301;	
double[] X1 =new double[M];
double[] X2 =new double[M];
double[] X3 =new double[M];

double[] XX1 =new double[M];
double[] XX2 =new double[M];
double[] XX3 =new double[M];

double[] XX1d =new double[M];
double[] XX2d =new double[M];
double[] XX3d =new double[M];


X1[0]=2.0;
X2[0]=3.0;
X3[0]=2.0;

double beta=0.001;	

double d1=0.2;
double d2=0.2;
double d3=5.7;

for(int i=0;i<M-1;i++)
{
X1[i+1]=(((-X2[i]-X3[i]))*beta)+X1[i];
X2[i+1]=((X1[i]+d1*X2[i])*beta)+X2[i];
X3[i+1]=((((X1[i]-d3)*X3[i])+d2)*beta)+X3[i];	
}

XX1[0]=2.0;
XX2[0]=3.0;
XX3[0]=2.0;

for(int i=0;i<M-1;i++)
{
XX1[i+1]=(((-XX2[i]-XX3[i]))*beta)+XX1[i];
XX2[i+1]=((XX1[i]+x[0]*XX2[i])*beta)+XX2[i];
XX3[i+1]=((((XX1[i]-x[2])*XX3[i])+x[1])*beta)+XX3[i];
}

double s1=0.0;
 

double[] Xdata=new double[3*M];
double[] Xcalc=new double[3*M];

for(int i=0;i<M;i++)
{Xdata[i]=X1[i];Xcalc[i]=XX1[i];}
int t1=0;
for(int i=M;i<2*M;i++)
{Xdata[i]=X2[t1];Xcalc[i]=XX2[t1];t1++;}
int t2=0;
for(int i=2*M;i<3*M;i++)
{Xdata[i]=X3[t2];Xcalc[i]=XX3[t2];t2++;}

for(int i=0;i<3*M;i++)
{s1+=(Xdata[i]-Xcalc[i])*(Xdata[i]-Xcalc[i]);}

double error=Math.sqrt(s1)/(double)(3*M);
return error;
}	
	
}


class freq extends f_xj 
{
double func(double x[])
{
  
   double[] t=new double[101];
   double dt=0.1;
   for(int i=0;i<101;i++)
   {t[i]=i*dt;}
 
    double phi=2.0*Math.PI/100.0; 

    
   double[] y=new double[t.length];
  
   double[] yy =new double[t.length];
   
   
     
      
      for(int i=0;i<t.length;i++)
      {y[i]=1.0*Math.sin(5.0*t[i]*phi-(1.5*(4.8*t[i]*phi+(2.0*Math.sin(4.9*t[i]*phi)))));}
     
	  for(int i=0;i<t.length;i++)
      {yy[i]=x[0]*Math.sin(x[1]*t[i]*phi-(x[2]*(x[3]*t[i]*phi+(x[4]*Math.sin(x[5]*t[i]*phi)))));}
	  
 
 
   
   double sum1=0.0; 
   for(int i=0;i<=t.length-1;i++)
   {sum1+=(y[i]-yy[i])*(y[i]-yy[i]);}
   sum1=Math.sqrt(sum1)/(double)t.length;
 
  
   double ret=sum1  ;
  


return ret  ;	
}	

}



class parf5 extends f_xj
{
// Duffing
public double func(double x[]) 
{
int M=301;	
double[] X =new double[M];
double[] V =new double[M];
double[] X1 =new double[M];
double[] V1 =new double[M];
X[0]=0.1;
V[0]=0.1;
double d1=0.4;
double d2=1.1;
double d3=1.0;
double bb=1.0;
double aa=0.0;
double h=(bb-aa)/(double)M;
for(int i=0;i<M-1;i++)
{
X[i+1]=(V[i]*h)+X[i];
V[i+1]=((-d1*V[i]+d2*X[i]-d3*X[i]*X[i]*X[i]+2.1*Math.sin(1.8*h*(double)i))*h)+V[i];	
}
X1[0]=0.1;
V1[0]=0.1;
for(int i=0;i<M-1;i++)
{
X1[i+1]=(V1[i]*h)+X1[i];
V1[i+1]=((-x[0]*V1[i]+x[1]*X1[i]-x[2]*X1[i]*X1[i]*X1[i]+2.1*Math.sin(1.8*h*(double)i))*h)+V1[i];	
}
double s1=0.0;


double[] Xdata=new double[2*M];
double[] Xcalc=new double[2*M];

for(int i=0;i<M;i++)
{Xdata[i]=X[i];Xcalc[i]=X1[i];}

int t1=0;
for(int i=M;i<2*M;i++)
{Xdata[i]=V[t1];Xcalc[i]=V1[t1];t1++;}

 

for(int i=0;i<2*M;i++)
{s1+=(Xdata[i]-Xcalc[i])*(Xdata[i]-Xcalc[i]);}

double error=Math.sqrt(s1)/(double)(2*M);
return error;



}	
	
}



public class harris_hawks_optimization_test
{
public static void main(String args[])
{	
                 //f20 levy                
                   //   double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f20 levy
                   //  double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
      
               //f36 ackley
                  //  double[] Lower={-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768,-32.768};//  f36 ackley
                  //   double[] Upper={32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768,32.768};
               
               // f11 griewank
                    // double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                    // double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0};
  
               // f34 Rastrigin
                   // double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f34 Rastrigin
                   // double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12};
      
               //f19 zakharov
                    //  double[] Lower={-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0};//  f19 Zakharov
                    //  double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0};                  
                
               // f101 alpine//
                     //  double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0, };//  f11 griewank
                     //  double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0 };
            
              //f41 penal1
                   //   double[] Lower={-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,};//  f41 Penalized
                   //   double[] Upper={5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0};

              // f42 Penalized 2
                  //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                  //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
           
              //f123 // Quintic   function     x(pi,pi)   
                   //   double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                   //   double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
     
               //f125 // Csendes   function     x(pi,pi)   
                  //  double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                  //  double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
                
               // f29 Schaffer
                 //   double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                 //   double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
 
               // f108 Salomon
                 //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                 //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
    
               // f133  Inverted cosine
                  //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                  //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
             
               //f152  Wavy function
                    //  double[] Lower={-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14,-3.14};//  f31 sphere
                    //  double[] Upper={3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14,3.14 };
            
               // f146 Hyperellipsoid Function 
                       // double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                       // double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
                 
               // f107 Path
                    //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                    //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
        
               // f31 Sphere
                      double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                       double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
  
                  
                 
               // f30 Rosenbrock
                   //  double[] Lower={-2.048,-2.048,-2.0480,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.0480,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.0480,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048,-2.048};//  f30 Rosenbrock
                   //  double[] Upper={2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048,2.048};

               //f27 sche
                 //   double[] Lower={-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,-5.0,};//  f40 step
                 //   double[] Upper={5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0};
                
               // f143 Schwefel 2.23 Function 
                  //   double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                  //   double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
           
               // f141  Schwefel 2.25 Function
                 // double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                 // double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
              
               //f121 Brown
                  //   double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                  //   double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
              
               //f122 // Streched V Sine Wave   function     x(pi,pi)   
                //    double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                //    double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
               
               //f124 // Powell   function     x(pi,pi)   
                //   double[] Lower={-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,-10.0,};//  f11 griewank
                //   double[] Upper={10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,};
           
               // f130 Sum of different power
                   //   double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                   //   double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
            
               // f131 High conditioned elliptic
                  //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                  //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
            
               // f132  Sum squares
                  //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                  //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
              
               //  f149   // Bent Cigar 
                 //  double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                 //  double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
             
               //  f150   // Discus
                    //double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                    //double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
             
                 //f151  // Different Powers Function  
                   //   double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                   //   double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };

                   
                 //f552  // Bohachevsky  
                       //double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                       //double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };

                       //f553  // Deb01  
                    //   double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                    //   double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };
 
                        //f554  // Mishra01  
                      // double[] Lower={-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12,-5.12};//  f31 sphere
                      // double[] Upper={5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12,5.12 };

    
          //         double[] Lower={ 0.0,  0.0, 3.28, 5.36,  3.0, 3.22, 3.22, 1.24, 8.06, 7.65, 7.65, 4.71,  28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74 ,28.74};
          //       double[] Upper={18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0,  40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00, 40.00};
                            //f123 // Quintic   function     x(pi,pi)   
               //double[] Lower={-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,-100.0,};//  f11 griewank
               //double[] Upper={100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,100.0,};
     
               // cec 2006 1
               //double[] Lower={0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,  0.0,  0.0,  0.0, 0.0};
               //double[] Upper={1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,100.0,100.0,100.0, 1.0};
               
               // cec 2006 2
               //double[] Lower={ 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,   0.0,  0.0,  0.0,  0.0,  0.0,   0.0};
               //double[] Upper={10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,  10.0, 10.0, 10.0, 10.0, 10.0,  10.0};
               
                // cec 2006 3
               //double[] Lower={ 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0   };
               //double[] Upper={ 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0   };
              
              
               // cec 2006 4
               //double[] Lower={ 78.0,  33.0, 27.0, 27.0,  27.0 };
               //double[] Upper={102.0,  45.0, 45.0, 45.0,  45.0 };
              
               // cec 2006 5
               //double[] Lower={   0.0,    0.0, -0.55,  -0.55};
               //double[] Upper={1200.0, 1200.0,  0.55,   0.55};
              
               // cec 2006 6
               //double[] Lower={   13.0,    0.0,  };
               //double[] Upper={  100.0,  100.0,   };
               
               // cec 2006 7
               //double[] Lower={  -10.0,   -10.0,   -10.0,   -10.0,   -10.0,   -10.0,   -10.0,   -10.0,   -10.0,   -10.0,  };
               //double[] Upper={   10.0,    10.0,    10.0,    10.0,    10.0,    10.0,    10.0,    10.0,    10.0,    10.0,};
               
                // cec 2006 8
               //double[] Lower={    0.0,     0.0,     };
               //double[] Upper={   10.0,    10.0,     };
              
               // cec 2006 9
               //double[] Lower={  -10.0,   -10.0,  -10.0,   -10.0, -10.0,   -10.0, -10.0,       };
               //double[] Upper={   10.0,    10.0,   10.0,    10.0,  10.0,    10.0,  10.0,       };         
               
               // cec 2006 10
               //double[] Lower={   100.0,  1000.0,  1000.0,   10.0,    10.0,   10.0,    10.0,   10.0 };
               //double[] Upper={ 10000.0, 10000.0, 10000.0, 1000.0,  1000.0, 1000.0,  1000.0, 1000.0 }; 
               
                // cec 2006 11
               //double[] Lower={ -1.0, -1.0 };
               //double[] Upper={  1.0,  1.0 }; 
               
                 // cec 2006 13
               //double[] Lower={ -2.3, -2.3, -3.2, -3.2, -3.2 };
               //double[] Upper={  2.3,  2.3,  3.2,  3.2,  3.2}; 
               
                // cec 2006 14
               //double[] Lower={  0.0,  0.0,   0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, };
               //double[] Upper={ 10.0, 10.0,  10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0,   }; 
               
                // cec 2006 15
              // double[] Lower={  0.0,  0.0,   0.0,      };
              // double[] Upper={ 10.0, 10.0,  10.0,      }; 
               
                 // cec 2006 17
              // double[] Lower={   0.0,    0.0,   340.0,   340.0, -1000.0,  0.0  };
              // double[] Upper={ 400.0, 1000.0,   420.0,   420.0,  1000.0,  0.5236  }; 
               
                // cec 2006 18
               //double[] Lower={ -10.0, -10.0,   -10.0,  -10.0,  -10.0,  -10.0,  -10.0,  -10.0,   0.0  };
               //double[] Upper={  10.0,  10.0,    10.0,   10.0,   10.0,   10.0,   10.0,   10.0,  20.0  }; 
            
               // cec 2006 21
              // double[] Lower={     0.0,   0.0,   0.0,  100.0,    6.3,   5.9,    4.5,    };
              // double[] Upper={  1000.0,  40.0,  40.0,  300.0,    6.7,   6.4,    6.25,   }; 
               
               // cec 2006 22
               //double[] Lower={      0.0,     0.0,     0.0,      0.0,    0.0,   0.0,    0.0, 100.00,   100.00,   100.01, 100.00,   100.0,   0.0,   0.0,    0.0,  0.01, 0.01, -4.70,  -4.70, -4.70, -4.70,  -4.70   };
               //double[] Upper={  20000.0,   1.0e6,   1.0e6,    1.0e6,    4e7,   4e7,    4e7, 299.99,   399.99,   300.00, 400.00,   600.0, 500.0, 500.0,  500.0, 300.0, 400 ,  6.25,   6.25,  6.25,  6.25,   6.25   }; 
               
               
               // cec 2006 23
               //double[] Lower={   0.0,    0.0,   0.0,   0.0,    0.0,   0.0,   0.0,    0.0,  0.01        };
               //double[] Upper={ 300.0,  300.0, 100.0, 200.0,  100.0, 300.0, 100.0,  200.0,  0.03        }; 
               
                // cec 2006 24
               //double[] Lower={   0.0,    0.0,    };
               //double[] Upper={   3.0,    4.0,    }; 
               
                // three bar truss
               //double[] Lower={   0.0,    0.0,    };
               //double[] Upper={   1.0,    1.0,    }; 
               
               // tension compression design
               //double[] Lower={   0.05,    0.25,  2.00   };
               //double[] Upper={   2.00,    1.30, 15.00   }; 
               
               // speed reducer
               //double[] Lower={   2.6,  0.7,   17.0,  7.3,  7.3,  2.9,  5.0   };
               //double[] Upper={   3.6,  0.8,   28.0,  8.3,  8.3,  3.9,  5.5   }; 
               
               // welded beam
               //double[] Lower={ 0.1,   0.1,   0.1,  0.1  };
               //double[] Upper={ 2.0,  10.0,  10.0,  2.0  };
               
               // frac01
               //double[] Lower={ 0.0,   0.0,      };
               //double[] Upper={ 200.0,  200.0,     };
               
                // frac02
               //double[] Lower={ 1.0,    0.0,      };
               //double[] Upper={ 2.0,  200.0,     };
               
               
               // frac03
               //double[] Lower={ 0.0,    0.0,      };
               //double[] Upper={ 1.0,    1.0,     };
               
               // frac04
               //double[] Lower={ 0.0,    0.0,      };
               //double[] Upper={ 10.0,    10.0,     };
               
               // frac05
               //double[] Lower={ 1.0,    1.0,      };
               //double[] Upper={ 10.0,    10.0,     };
               
               // frac06
               //double[] Lower={  1.0,   1.0,      };
               //double[] Upper={ 2.25,   4.0,     };
               
               // frac07
               //double[] Lower={  1.0,   1.0,      };
               //double[] Upper={  3.0,   3.0,     };
              
               // frac08
               //double[] Lower={  1.5,    0.0,      };
               //double[] Upper={  23.0,   30.0,     };
               
               
               // frac08
              // double[] Lower={  1.0,    1.0,      };
              // double[] Upper={  3.0,    4.0,     };
               
               //fconstr1
               //double[] Lower={  0.1,    0.0,      };
               //double[] Upper={  1.0,    5.0,     };
               
               //fconstr2
               //double[] Lower={  0.0,      0.0,      };
               //double[] Upper={  Math.PI,  Math.PI,     };

               //fconstr3
              // double[] Lower={  0.0,      0.0,      };
              // double[] Upper={  7.0,      7.0,     };               
             
              //threebartruss
          //  double F=10;
		//	double E=2e5;
		//	double L=200;
		//	double sigma=10;
          //     double[] Lower={  (F/sigma),  Math.sqrt(2)*(F/sigma), Math.sqrt(2)*(F/sigma), (F/sigma)      };
            //   double[] Upper={  3.0*(F/sigma),  3.0*(F/sigma), 3.0*(F/sigma), 3.0*(F/sigma)   };   
            
          //disk brake
          // double[] Lower={ 0.125,0.1,0.1,0.125 };
          // double[] Upper={ 5.0,10.0,10.0,5.0  }; 
          
                 
// 				 	double[] Lower={1e-20,     0.0,  0.0,  100.0     };
// 				 	double[] Upper={2.0,   10000.0,  5.0, 100000000.0  };      
//                                    //Io     //Rs    //n     //Rshunt
                    //par f1
                    //double[] Lower={0.0,   0.0, 0.0 };
                    //double[] Upper={20.0, 50.0,10.0 };

 
                    //par f2
                    //double[] Lower={  0.0,   0.0,  0.0 };
                    //double[] Upper={ 50.0,  10.0, 50.0 };
                    
           
                     //par f3
					//double[] Lower={  0.0,   0.0,  0.0 };
                    //double[] Upper={  5.0,   5.0, 10.0 };
                    
                    //freq
                   // double[] Lower={ -6.40, -6.40, -6.40, -6.40, -6.40, -6.40   };
                   // double[] Upper={  6.35,  6.35,  6.35,  6.35,  6.35,  6.35  };
                   
 
				   
				    //par f5
 					 //double[] Lower={  0.0,   0.0,  0.0 };
                     //double[] Upper={  5.0,   5.0,  5.0 };
                    
       //parf5 ff=new parf5();
    
f31 ff=new f31();
// dam2 ff=new dam2(); 
int Maxiter=100;
int N=36;

  


  harris_hawks_optimization wo=new   harris_hawks_optimization(ff,N,Lower,Upper,Maxiter);  
 


 
long startTime = System.currentTimeMillis();
wo.toStringnew();
 
long endTime   = System.currentTimeMillis();
           long totalTime = endTime - startTime;
           System.out.println((totalTime/1000.0)+" sec");
 
  harris_hawks_optimization_test.main(args);




}	












}