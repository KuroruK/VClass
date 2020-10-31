package com.example.vclasslogin;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import joinery.DataFrame;


class course{
	String code,name,CHs,Sec,Instructor,coordinator;
	course(){};

	@Override
	public String toString()
	{
		return code+":"+name+","+Sec+"\n   "+Instructor+" & "+coordinator+","+CHs+"\n";
	}
}
class timeSlot implements Comparable<timeSlot> {
	String name, start, end;

	public Time getStartTime(){
		String[] arr=start.split(":");
		int h=Integer.parseInt(arr[0]);
		if(h<6)
			h+=12;
		int m=Integer.parseInt(arr[1]);
		Time a=new Time(h,m);

		return a;
	}
	public Time getEndTime(){
		String[] arr=end.split(":");
		int h=Integer.parseInt(arr[0]);
		if(h<6)
			h+=12;
		int m=Integer.parseInt(arr[1]);
		Time a=new Time(h,m);

		return a;
	}

	@Override
	public String toString() {
		return name + ":" + start + " to " + end;
	}

	@Override
	public int compareTo(timeSlot o) {
		String a[] = start.split(":");
		String b[] = o.start.split(":");

		int h1 = Integer.parseInt(a[0]) * 100;
		if (h1 / 100 < 6)
			h1 += 100000;
		int m1 = Integer.parseInt(a[1]);
		int h2 = Integer.parseInt(b[0]) * 100;
		if (h2 / 100 < 6)
			h2 += 100000;
		int m2 = Integer.parseInt(a[1]);

		return (int) (h1 + m1 - h2 - m2);
	}
}

public class csvLoader {
	ArrayList<course> crs;

	public ArrayList<course> getCourses(Context cont,String file) {
		crs = new ArrayList<course>();
		DataFrame<Object> df;
		try {
			new DataFrame<>();
			Log.v("gerCourses",cont.toString());
			df = DataFrame.readCsv(cont.getAssets().open(file));

			String ccode = "";
			String cname = "";
			String cCHs = "";
			String cCoordinator = "";


			for (int i = 0; i < df.length(); i++) {

				if (df.get(i, 0) != null) {
					course temp = new course();
					for (int j = 1; j < df.size(); j++) {
						switch (j) {
							case 1:
								if (df.get(i, j) == null)
									temp.code = ccode;
								else {
									temp.code = ccode = df.get(i, j).toString();
								}

								break;
							case 2:
								if (df.get(i, j) == null)
									temp.name = cname;
								else {
									temp.name = cname = df.get(i, j).toString();
								}
								break;
							case 3:
								if (df.get(i, j) == null)
									temp.CHs = cCHs;
								else {
									temp.CHs = cCHs = df.get(i, j).toString();
								}
								break;
							case 4:
								temp.Sec = df.get(i, j).toString();
								break;
							case 5:
								temp.Instructor = df.get(i, j).toString();
								break;
							case 6:
								if (df.get(i, j) == null)
									temp.coordinator = cCoordinator;
								else {
									temp.coordinator = cCoordinator = df.get(i, j).toString();
								}
								break;
							default:
								break;
						}

					}
					crs.add(temp);
				}

			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		return crs;
	}


	ArrayList<timeSlot> table;

	public ArrayList<timeSlot> getTable(Context cont,String name) {

		table = new ArrayList<timeSlot>();
		DataFrame<Object> df;
		try {
			new DataFrame<>();
			df = DataFrame.readCsv(cont.getAssets().open(name));


			Set<Object> a = df.columns();
			ArrayList<Object> times = new ArrayList<Object>(a);

			DataFrame<String> dfs = df.cast(String.class);

			timeSlot temp = null;
			for (int i = 0; i < dfs.length(); i++) {

				for (int j = 0; j < dfs.size(); j++) {
					//System.out.println(dfs.get(i,j));

					if (dfs.get(i, j) != null && !dfs.get(i, j).equals("c")) {

						temp = new timeSlot();
						temp.name = dfs.get(i, j).toString();
						temp.start = times.get(j).toString();
						//temp.end????
						table.add(temp);

					} else if (dfs.get(i, j) == null && j > 0 && dfs.get(i, j - 1) != null) {
						temp.end = times.get(j - 1).toString();
					}
				}


			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		return table;
	}
}
