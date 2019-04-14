import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Assignment1 {

	public static void main(String[] args) throws IOException {
		RandomAccessFile Datafile = new RandomAccessFile("SampleDataFile.bin","rw");
		RandomAccessFile indexfile = new RandomAccessFile("indexfile.bin","rw");
		indexfile.setLength(0);
//		Createindexfileonce(indexfile,Datafile);
		char c='y';
		while(c=='y')
		{
		System.out.println("Which operation You wanna do ?");
		System.out.println("show Datafile (1)");
		System.out.println("show index file (2)");
		System.out.println("Search for A reCord in Datafile (3)");
		System.out.println("Update Record in datafile (4)");
		System.out.println("Add Record to data file (5)");
		System.out.println("Delete Record from data file (6)");
		int choose;
		Scanner input=new Scanner(System.in);
		choose=input.nextInt();
		switch(choose) {
		case 1:showdatafilecontent(Datafile);
			break;
		case 2 :showindexfilecontent(indexfile);
			break;
		case 3:SreachforRecord(indexfile,Datafile);
			break;
		case 4:UpdateRecordindatafile(indexfile,Datafile);
			break;
		case 5:Addrecord(indexfile,Datafile);
			break;
		case 6:DeleteRecord(indexfile,Datafile);
			break;
		}
		System.out.println("Do you wanna do another operation ? yes(y) --- no (n)");
		c=input.next().charAt(0);
		
		}

		
			
	
		


		
	}
	public static void DeleteRecord(RandomAccessFile indexfile, RandomAccessFile Datafile) throws IOException
	{
		indexfile.seek(0);
		Datafile.seek(0);
		System.out.println("Enter Record ID that you want to Delete ");
		int EnterID;
		Scanner in = new Scanner(System.in);
		EnterID=in.nextInt();
		int offset=getrecordoffsetfromindex(indexfile,EnterID);
		if(offset!=-1)
		{
			
			Datafile.seek(offset);
			Datafile.writeChar('*');
			System.out.println("Record Successfully deleted");
			
		}
		else 
			System.out.println("Sorry ID not Esits");
		
		
		indexfile.seek(0);
		int offsett=0;
		while(indexfile.getFilePointer()<indexfile.length())
		{
			
			offsett = (int) indexfile.getFilePointer();
			
			int id,indx;
			id=indexfile.readInt();
			indx=indexfile.readInt();
			if(EnterID==id)
			{
				
				break;
				
			}
	
		}
		
		int newoffset=offsett/8;
		newoffset++;
		for(int i=newoffset;i<indexfile.length()/8; i++)
		{
			
		indexfile.seek(i*8);
		int id ,price;
		id=indexfile.readInt();
		price=indexfile.readInt();
		indexfile.seek((i-1)*8);
		indexfile.writeInt(id);
		indexfile.writeInt(price);
		}
		indexfile.setLength(indexfile.length()-8);;
		
		
		
	}
	public static void showdatafilecontent(RandomAccessFile Datafile) throws IOException

	{
		
		
		Datafile.seek(0);
		System.out.println("ID   |"+"Price   |"+" qty");
		while(Datafile.getFilePointer()<Datafile.length())
		{
			int id , price,qty;
			id=Datafile.readInt();
			price=Datafile.readInt();
			qty=Datafile.readInt();
			System.out.println(id+"  | "+price+"    | "+qty);
			
		
		}
		
		
		
	}
	public static void showindexfilecontent(RandomAccessFile indexfile) throws IOException
	{
		indexfile.seek(0);
		System.out.println("id   |"+" offset  " );
		while(indexfile.getFilePointer()<indexfile.length())
		{
			
			int id , index;
			id=indexfile.readInt();
			index=indexfile.readInt();
			System.out.println(id+"   |"+index);
			
		
		}
		
		
	}
	public static int getrecordoffsetfromindex(RandomAccessFile indexfile,int ID) throws IOException
	{
		
//		*******Binary search on index file **************
		
		indexfile.seek(0);

			int l=0,r=(int) (indexfile.length()/8)-1;
			
			while(l<=r)
			{
				int mid =l+(r-l)/2;
				int id ,offset;
				indexfile.seek(mid*8);
				id=indexfile.readInt();
				offset=indexfile.readInt();
				if(ID==id)
				{
					return offset;
				}
				if(id<ID)
				{
					l=mid+1;
				}
				else
					r=mid-1;
				
				
			}
				
				
	
			return -1;
		
		
	}
	public static int[] getrecordfromdatafile(RandomAccessFile Datafile,int offset) throws IOException
	{
		Datafile.seek(0);
		Datafile.seek(offset);
		int Record[]={Datafile.readInt(),Datafile.readInt(),Datafile.readInt()};
		return Record;
		
		
	}
	public static void Createindexfileonce(RandomAccessFile indexfile,RandomAccessFile Datafile) throws IOException
	{
		
		
		ArrayList<Integer> ID= new ArrayList<Integer>();
		ArrayList<Integer> offset= new ArrayList<Integer>();
		
		Datafile.seek(0);
		while(Datafile.getFilePointer()<Datafile.length())
		{
			int id,m,n;
			id=Datafile.readInt();
			m=Datafile.readInt();
			n=Datafile.readInt();
			ID.add(id);
		}
		Collections.sort(ID);
		for(int i=0;i<ID.size();i++)
		{
			
		Datafile.seek(0);
		while(Datafile.getFilePointer()<Datafile.length())
		{
			int index=(int) Datafile.getFilePointer();
			int id,m,n;
			id=Datafile.readInt();
			m=Datafile.readInt();
			n=Datafile.readInt();
			if(ID.get(i)==id)
			{
				offset.add(index);
			}
			
		}
		
		
		}
	
//		****************write to file ****************
		for(int i=0;i<ID.size();i++)
		{
			indexfile.writeInt(ID.get(i));
			indexfile.writeInt(offset.get(i));
			
		}
		indexfile.
		
	}
	
	public static void SreachforRecord(RandomAccessFile indexfile,RandomAccessFile Datafile) throws IOException
	{
		
		
		System.out.println("Enter the id to search in index file  ");
		int EnterID;
		Scanner input=new Scanner(System.in);
		EnterID=input.nextInt();
		if(getrecordoffsetfromindex(indexfile,EnterID)!=-1)
		{
			int R[]=getrecordfromdatafile(Datafile,getrecordoffsetfromindex(indexfile,EnterID));
			System.out.println("Record Esists in Data file and Here are the Data "+R[0]+"  "+R[1]+"  "+R[2]+"  ");
			
			
		}
		else 
			System.out.println("Sorry ID not Esits ");
		
		
		
	}
	public static void UpdateRecordindatafile(RandomAccessFile indexfile,RandomAccessFile Datafile) throws IOException
	{
		indexfile.seek(0);
		Datafile.seek(0);
		System.out.println("Enter Record ID that you want to updata ");
		int EnterID;
		Scanner in = new Scanner(System.in);
		EnterID=in.nextInt();
		int offset=getrecordoffsetfromindex(indexfile,EnterID);
		if(offset!=-1)
		{
			System.out.println("(1) to chabge price (2 to change (qty) (3) to change both)");
			int choose ;
			choose=in.nextInt();
			if(choose==1)
			{
				int p;
				System.out.println("Enter new price ");
				p=in.nextInt();
				
				Datafile.seek(offset+4);
				Datafile.writeInt(p);
				System.out.println("price successfuly updated");
				
				
				
				
			}
			if(choose==2)
			{
				int q;
				System.out.println("Enter new qty ");
				q=in.nextInt();
				Datafile.seek(offset+8);
				Datafile.writeInt(q);
				System.out.println("price successfuly updated");
				
				
				
				
			}
			
			if(choose==3)
			{
			int price,qty;
			System.out.println("Enter new  product price ");
			price =in.nextInt();
			System.out.println("Enter new  product qty ");
			qty =in.nextInt();
			Datafile.seek(offset+4);
			Datafile.writeInt(price);
			Datafile.writeInt(qty);
			System.out.println("Record Successfully updated");
			}
			
		}
			else 
			System.out.println("Sorry ID not Esits ");
		
		
	}
	public static void Addrecord(RandomAccessFile indexfile,RandomAccessFile Datafile) throws IOException
	{
		
		Scanner in = new Scanner(System.in);
		boolean flage=true;
		int ID = 0,price,qty;
		while(flage)
		{
		System.out.println("Enter The new  Record to insert in datfile");
		System.out.println("Enter ID ");
		ID=in.nextInt();
		System.out.println("Enter price ");
		price=in.nextInt();
		System.out.println("Enter qty ");
		qty=in.nextInt();
		if(getrecordoffsetfromindex(indexfile,ID)==-1) {
			
			Datafile.seek(Datafile.length());
			Datafile.writeInt(ID);
			Datafile.writeInt(price);
			Datafile.writeInt(qty);
			System.out.println("Record successfully added to Datafile ");
			flage=false;
			
			
		}
		else
			System.out.println("Sorry The ID should be uniqe for each produce Enter Record With uniqe ID");
		
		
		}
		
		
		
		Datafile.seek(0);
		indexfile.seek(0);
		int offset=0;
		while(indexfile.getFilePointer()<indexfile.length())
		{
			
			offset = (int) indexfile.getFilePointer();
			int id,indx;
			id=indexfile.readInt();
			indx=indexfile.readInt();
			if(ID<id)
			{
				break;
				
			}
	
		}
		
		indexfile.seek(indexfile.length()-8);
		int a,b;
		a=indexfile.readInt();
		b=indexfile.readInt();
		if(ID>a)
		{
			indexfile.writeInt(ID);
			indexfile.writeInt((int) (Datafile.length()-12));
		}
		
		else
		{
		indexfile.seek(0);
		for(int i=(int) ((indexfile.length()-8)/8); i>=offset/8;i--)
		{
			
			indexfile.seek(i*8);
			
			a=indexfile.readInt();
			b=indexfile.readInt();
			indexfile.writeInt(a);
			indexfile.writeInt(b);
			
		}
		
		indexfile.seek(offset);
		indexfile.writeInt(ID);
		indexfile.writeInt((int) (Datafile.length()-12));
		
		
		}
		
		
	}








} 
