package com.mobao360.sunshine;

public class calculate{
	public double[] ca;
	public double sum=0;
	public double result=0;
	public int num=0;
    public calculate(double[] cal){
    	this.ca=cal;
    }
   public  boolean DoubleDemo(double obj11,double obj21){
	   Double obj1 = new Double(obj11);
	     Double obj2 = new Double(obj21);
	int retval = obj1.compareTo(obj2);
     
     if(retval > 0) {
      
        return false;
     }
     else{
        return true;
     }
   }
   public double rank(){
	   int len=ca.length;
	   double t;
	   for(int j=len-1;j>0;j--){
	   for(int i=0;i<j;i++){
		   if(DoubleDemo(ca[i+1],ca[i])){
			   t=ca[i];
			   ca[i]=ca[i+1];
			   ca[i+1]=t;
		   }
	   }
	   }
	   for(int i=3;i<len-3;i++){
		   sum+=ca[i];
		   num++;
	   }
	   result=sum/(double)num;
	  
	   return  result;
   }
}
