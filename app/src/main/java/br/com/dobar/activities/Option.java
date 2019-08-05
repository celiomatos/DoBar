package br.com.dobar.activities;

public class Option implements Comparable<Option>{
	private String name;
	private String data;
	private String path;
	private boolean folder;
	private boolean parent;

	/**
	 * 
	 * @param n
	 * @param d
	 * @param p
	 * @param folder
	 * @param parent
	 */
	public Option(String n,String d,String p, boolean folder, boolean parent)
	{
		name = n;
		data = d;
		path = p;
		this.folder = folder;
		this.parent = parent;
	}
	/**
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * 
	 * @return
	 */
	public String getData()
	{
		return data;
	}
	/**
	 * 
	 * @return
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * 
	 */
	public int compareTo(Option o) {
		if(this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
	/**
	 * 
	 * @return
	 */
	public boolean isFolder() {
		return folder;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isParent() {
		return parent;
	}
}
