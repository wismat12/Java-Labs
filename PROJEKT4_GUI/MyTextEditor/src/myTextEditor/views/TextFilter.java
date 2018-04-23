package myTextEditor.views;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.StyleConstants;

public class TextFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		
		if(f.getAbsolutePath().endsWith(".txt")||(f.getAbsolutePath().endsWith(".rtf"))||(f.getAbsolutePath().endsWith(".java"))||(f.isDirectory())){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public String getDescription() {
	
		return "Plik txt/rtf/java";
	}

}

		
