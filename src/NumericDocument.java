import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
 
public class NumericDocument extends PlainDocument {
	
	private static final long serialVersionUID = 5445844557048201311L;
	
	//Variables
	protected int decimalPrecision = 0;
	protected boolean allowNegative = false;
  
	// Constructor
	public NumericDocument(int decimalPrecision, boolean allowNegative) {
    	 super();
          this.decimalPrecision = decimalPrecision;
          this.allowNegative = allowNegative;
	}
   
    //Insert string method
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		
		if (str != null){
        
			if (this.isNumeric(str) == false && str.equals(",") == false && str.equals("-") == false){ //First, is it a valid character?
				Toolkit.getDefaultToolkit().beep();
				return;
			} else if (str.equals(",") == true && super.getText(0, super.getLength()).contains(",") == true){ //Next, can we place a decimal here?
				Toolkit.getDefaultToolkit().beep();
				return;
			} else if (this.isNumeric(str) == true && super.getText(0, super.getLength()).indexOf(",") != -1 && offset>super.getText(0, super.getLength()).indexOf(",") && super.getLength()-super.getText(0, super.getLength()).indexOf(",")>decimalPrecision && decimalPrecision > 0){ //Next, do we get past the decimal precision limit?
				Toolkit.getDefaultToolkit().beep();
				return;
			} else if (str.equals("-") == true && (offset != 0 || allowNegative == false)){ //Next, can we put a negative sign?
				Toolkit.getDefaultToolkit().beep();
				return;
			}
   
			// All is fine, so add the character to the text box
			super.insertString(offset, str, attr);
		}
        
		return;
     
	}
     
     public boolean isNumeric(String string) {
	 
    	 boolean result = false;
	 
    	 try {  
    
    		 Double.parseDouble(string);  
    		 result = true;;  

    	 } catch(Exception ex) {  
       
    		 result = false;  
    
    	 }
    	 return result;
	 
     }
     
}