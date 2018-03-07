//Complex.java

package process.algorithms.imageTrans;

public class Complex 
{	
	public double re;
	public double im;
	
	public Complex()
	{
		this.re = 0;
		this.im = 0;
	}
	
	public Complex(double re)
	{
		this.re = re;
		this.im = re;
	}
	
	public Complex(double re,double im)
	{
		this.re = re;
		this.im = im;
	}
	
	public void setRE(double re)
	{
		this.re=re;
	}
	
	public void setIM(double im)
	{
		this.im=im;
	}
	
	public double getRE()
	{
		return this.re;
	}
	
	public double getIM()
	{
		return this.im;
	}
	
	//加法	
	public Complex Add(Complex c1,Complex c2)
	{
		Complex c=new Complex(0,0);
		c.re=c1.re+c2.re;
		c.im=c1.im+c2.im;
		return c;
	}
	
	//减法
	public Complex Sub(Complex c1,Complex c2)
	{
		Complex c=new Complex(0,0);
		c.re=c1.re-c2.re;
		c.im=c1.im-c2.im;
		return c;
	}
	
	//乘法
	public Complex Mul(Complex c1,Complex c2)
	{
		Complex c = new Complex(0,0);
		c.re = c1.re*c2.re-c1.im*c2.im;
		c.im = c1.re*c2.im+c2.re*c1.im;
		return c;
	}
}