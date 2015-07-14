package br.eti.wjr.calculadora.uesc;

import android.text.Editable;
import android.text.TextWatcher;
import br.eti.wjr.util.NumericParser;

/**
 * Classe que define o filtro para o formato decimal dos créditos.
 * 
 * @author wjr
 * @since 1.1
 */
public class CreditoTextWatcher implements TextWatcher {
	private boolean isRecursion;
	
	private String before;
	
	
	public CreditoTextWatcher() {
		this.setIsRecursion(false);
	}
	
	
	public boolean isRecursion() {
		return isRecursion;
	}
	
	public void setIsRecursion(boolean isUserInput) {
		this.isRecursion = isUserInput;
	}
	
	public String getBefore() {
		return before;
	}
	
	public void setBefore(String before) {
		this.before = before;
	}
	
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (this.isRecursion() == true) {
			return;
		}
		this.setBefore(s.toString());
	}
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	public void afterTextChanged(Editable s) {
		int length = s.length();
		if (this.isRecursion() == true || length == 0) {
			return;
		}
		
		this.setIsRecursion(true);
		
		int index = s.toString().indexOf(".");
		if (index > -1) {
			if (index != 1 || ((length - index - 1) > 2)) {
				s.clear();
				s.append(this.getBefore());
			}
		} else {
			int value = NumericParser.tryParseInt(s.toString());
			if ((value < 10 && length > 1) || value > 10) {
				s.clear();
				s.append(this.getBefore());
			}
		}
		
		this.setIsRecursion(false);
	}
}
