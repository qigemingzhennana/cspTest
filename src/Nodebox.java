
public class Nodebox 
{
	int Nodetype =0;
	
	int key;
	Nodebox son=null;
	Nodebox brother=null;
	Nodebox brother1=null;
	
	//balance tree for each node
	boolean color=false;
	
	int value=-1;
	
	int add(int[] in)
	{
		int n=in.length;
		
		Nodebox p=this;
		Nodebox q=null;
		for(int i=0;i<n;i++)
		{
			q=p.son;
			int a=in[i];
			while(q!=null&&q.key!=a)
			{
				q=q.brother;
			}
			if(q==null)
			{
				q=new Nodebox();
				q.brother=p.son;
				p.son=q;
				q.key=a;
			}
			p=q;
		}
		
		if(p.value==-1)
		{
			value++;
			p.value=value;
		}
		
		return p.value;
	}
	
	int getValue(int [] in)
	{
		int n=in.length;
		
		Nodebox p=this;
		Nodebox q=null;
		
		for(int i=0;i<n;i++)
		{
			q=p.son;
			int a=in[i];
			while(q!=null&&q.key!=a)
			{
				q=q.brother;
			}
			if(q==null)
			{
				return -1;
			}
			p=q;
		}
		return p.value;
	}
	
}
