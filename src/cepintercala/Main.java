package cepintercala;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.io.RandomAccessFile;



class ComparadorCEP implements Comparator<Endereco>
{
	public int compare(Endereco e1, Endereco e2)
	{
		return e1.getCep().compareTo(e2.getCep());
	}
}


public class Main {
	
	public static void main(String[] args) throws Exception 
	{
		ArrayList<RandomAccessFile> lista = new ArrayList<RandomAccessFile>();
		ArrayList<File> file = new ArrayList<File>();
		
		RandomAccessFile f = new RandomAccessFile("cep.dat", "r");
		Endereco e = new Endereco();
		
		// C�DIGO PARA CRIAR UM ARQUIVO COM APENAS 80 CEPs PARA PROP�SITO DE TESTES
		/*
		RandomAccessFile saida = new RandomAccessFile("cep80.dat","rw");
		for(int i=0; i<80; i++) {
			f.seek(i*300);
			e.leEndereco(f);
			e.escreveEndereco(saida);
		}*/
		
		
		int k = 0;
		int count = 0;
		
		for(int i=0; i<8; i++) {
			
			file.add(new File("cep"+i+".dat"));
			lista.add(new RandomAccessFile(file.get(i), "rw"));
			
			count++;
			
			// C�DIGO PARA CASO UTILIZE O ARQUIVO COM 80 CEPs, COMENTAR C�DIGO ORIGINAL CASO V� UTILIZAR ESTE
			/*
			for(int j=0; j<10; j++) {
				saida.seek(k*300);
				k++;
				e.leEndereco(saida);
				e.escreveEndereco(lista.get(i));
				
			}*/
			
			// C�DIGO ORIGINAL - UTILIZA O ARQUIVO CEP.DAT ORIGINAL COM TODAS AS 699308 LINHAS
			
			try {
				for(int j=0; j<87414; j++) {
					f.seek(k*300);
					k++;
					e.leEndereco(f);
					e.escreveEndereco(lista.get(i));
				}
			} catch(java.io.EOFException error) {
				break;
			}
			
		}
		
		boolean abriu = true;
		
		while(abriu == true) {
			try {
				ArrayList<Endereco> a1 = new ArrayList<Endereco>();
				ArrayList<Endereco> a2 = new ArrayList<Endereco>();
				RandomAccessFile f2 = lista.get(0);
				RandomAccessFile f3 = lista.get(1);
				
				f2.seek(0);
				while(f2.getFilePointer() < f2.length()) {
					Endereco e2 = new Endereco();
					e2.leEndereco(f2);
					a1.add(e2);
				}
				
				f3.seek(0);
				while(f3.getFilePointer() < f3.length()) {
					Endereco e3 = new Endereco();
					e3.leEndereco(f3);
					a2.add(e3);
				}
				
				file.add(new File("cep"+Integer.toString(count)+".dat"));
				RandomAccessFile fnovo = new RandomAccessFile(file.get(file.size()-1), "rw");
				lista.add(fnovo);
				
				ComparadorCEP c1 = new ComparadorCEP();
				
				Collections.sort(a1, new ComparadorCEP());
				Collections.sort(a2, new ComparadorCEP());
				int somaListas = a1.size() + a2.size();
				
				for(int i=0; i < somaListas; i++) {
					int ordem = 0;
					try {
						ordem = c1.compare(a1.get(0), a2.get(0));
					} catch(java.lang.IndexOutOfBoundsException erro) {
						if(a1.size() == 0) {
							ordem = 1;
						} else {
							ordem = -1;
						}
					}
					
					if (ordem < 0) {
						a1.get(0).escreveEndereco(fnovo);
						a1.remove(0);
					} else if (ordem > 0){
						a2.get(0).escreveEndereco(fnovo);
						a2.remove(0);
					}
					
				}
				
				try(RandomAccessFile deletarArquivo = lista.get(0)){
					
				}
				catch(Exception ex){
				    ex.printStackTrace();
				}

				file.get(0).delete();
				file.remove(0);
				lista.remove(0);
				
				try(RandomAccessFile deletarArquivo = lista.get(0)){
					
				}
				catch(Exception ex){
				    ex.printStackTrace();
				}
				
				file.get(0).delete();
				file.remove(0);
				lista.remove(0);
				
				count++;
				
			} catch (java.lang.IndexOutOfBoundsException erro) {
				abriu = false;
				break;
			}
			
		}	
	}
}
