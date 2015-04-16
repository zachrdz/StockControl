/**
 * 
 */
package core.settings.models;

/**
 * @author zachary.rodriguez
 *
 */
public class Function {
	private int functionID;
	private String functionName;
	private String functionDesc;
	
	public Function(){
	}
	
	public Function(String name){
		this.functionName = name;
	}
	
	public int getFunctionID() {
		return functionID;
	}
	
	public void setFunctionID(int functionID) {
		this.functionID = functionID;
	}
	
	public String getFunctionName() {
		return functionName;
	}
	
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionDesc() {
		return functionDesc;
	}
	
	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	
	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Function) {
			Function ptr = (Function) v;
			retVal = ptr.functionName.equals(this.functionName);
		}

		return retVal;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.functionName != null ? this.functionName.hashCode() : 0);
		return hash;
	}
	
}
