package torresDeHanoi;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.ScrollPaneConstants;

/**
 * Clase que representa la interfaz gráfica principal para el juego de las Torres de Hanoi.
 * hereda jframe para la interacción con las pilas y las tablas (JTable).
 * @author Compiladores
 * @version 1.0
 */
public class VentanaPrincipalHanoi extends JFrame {
//CONSTANTES-------------------------------------------------------------------
//ATRIBUTOS DE LA CLASE---------------------------------------------------------
//ATRIBUTOS----------------------------------------------------------------------
	/*
	 * Pilas que representan las torres del juego
	 */
	Pila torreAPila;
	Pila torreBPila;
	Pila torreCPila;
	
	/*
	 * contador de la cantidad de movimientos del usuario
	 */
	int contNumMov=0;
	
	DefaultTableModel modeloTablaTorreA, modeloTablaTorreB,modeloTablaTorreC ;
	/*
	 * cantidad de discos seleccionados
	 */
	int objetivo=0;
	/*
	 * numero de la cantidad movimientos necesarios
	 */
	double numMinDeMovimientos=0.0;
	/*
	 * stop para ir mostrando la resolucion por pasos
	 */
	boolean stop= false;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable TorreA; //table q representa la torre A
	private JTable TorreB;//table q representa la torre B
	private JTable TorreC;//table q representa la torre C
	private JButton BtnB_A;
	private JButton BtnB_C;
	private JButton BtnC_A;
	private JButton BtnC_B;
	private JLabel lblMinMovimientos;
	private JLabel lblNumMovimientos;
	private JComboBox comboBoxDiscos;
						
//CONSTRUCTORES-----------------------------------------------------------------
//METODOS DE CLASES-------------------------------------------------------------
//METODOS GENERALES------------------------------------------------------------
//METODOS DE COMPORTAMIENTO------------------------------------------------------

	/*
	 * resetea el numero de movimientos, el numero minimo de movimientos necesarios
	 * y setea el box de discos q ve el usuario
	 */
	private void limpiar() {
		numMinDeMovimientos=0;
		contNumMov=0;
		comboBoxDiscos.setSelectedItem(String.valueOf(objetivo));
	}
	
	/*
	 * aumenta la cantidad de movimientos realizados
	 * setea el label numero de movimientos con el nuevo numero
	 */
	private void presentarCantidadMovimientos() {
		contNumMov++;
		lblNumMovimientos.setText(String.valueOf(contNumMov));
		
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalHanoi frame = new VentanaPrincipalHanoi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * @param:pila de origen (de donde se quiere sacar un nodo)
	 * @param1:pila de destino (donde se quiere meter un nodo)
	 * mueve del origen al destino (first out, first in)
	 * con joptionpane se construye un modal(dialog) q pregunte si el usuario quiere ver el siguiente paso
	 */
	private void moverPlataforma(Pila origen, Pila destino) {
		if (stop==false) {
			Nodo plataforma = new Nodo(origen.peek());
			origen.pop();
			destino.push(plataforma);
			
			presentarTorreA();
			presentarTorreB();
			presentarTorreC();
			presentarCantidadMovimientos();
			
			JOptionPane pane = new JOptionPane(
					"paso#" + lblNumMovimientos.getText()+ "\n\n desea continuar?",
					JOptionPane.QUESTION_MESSAGE,
					JOptionPane.YES_NO_OPTION
					);
			JDialog dialog= pane.createDialog("numero de pasos");
			dialog.setLocationRelativeTo(null);;
			dialog.setVisible(true);
			int opt= (int) pane.getValue();
			if(opt == JOptionPane.NO_OPTION) {
				stop=true;
			}
			
			
		}
	}
	
	/*
	 * @param:pila origen 
	 * @param1:pila auxiliar 
	 * @param2: pila destino
	 * resuelve el de forma recursiva el algoritmo de hanoi
	 */
	private void resolverHanoi(int n, Pila ori, Pila aux, Pila des) {
		if (stop) {
			return;
		}
		try {
			if(n==1) {
			moverPlataforma(ori, des);
			}
		else {
			resolverHanoi(n-1, ori, des, aux);
			moverPlataforma(ori, des);
			
			resolverHanoi(n-1, aux, ori, des);
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	/*
	 * limpia(resetea los contadores) y vuelve a iniciar el juego
	 */
	private void reiniciar() {
		try {
			if(!lblMinMovimientos.getText().equals("")) {
				limpiar();
				inciar();
			}
		} 
		catch (Exception e) {
			System.out.println("Error"+e.getMessage());
		}
	}
	/*
	 * inicia el juego con el objetivo seleccionado
	 */
	private void inciar() {
		try {
			torreAPila= new Pila();
			torreBPila= new Pila();
			torreCPila= new Pila();
			
			//setea en objetivo los discos seleccionados por el usuarios
			objetivo= Integer.parseInt(comboBoxDiscos.getSelectedItem().toString());
			
			//calcula el minimo de movimientos posibles
			numMinDeMovimientos = Math.pow(2, objetivo)-1;
			
			// pone los contadores en 0 (los valores de las variables al comenzar)
			lblNumMovimientos.setText(String.valueOf(contNumMov));
			lblMinMovimientos.setText(String.valueOf(String.format(" %.0f", numMinDeMovimientos)));
			
			//crea los discos visuales 
			 for (int i=objetivo; i>=1; i-- ) {
				 String disco="";
				 for (int x=i;x>0;x--) {
					disco += "#";
				}
				 torreAPila.push(new Nodo(disco));
			 }
			 presentarTorreA();
			 presentarTorreB();
			 presentarTorreC();
			 
			
		} catch (Exception e) {
			System.out.println("Error"+e.getMessage());
		}		
	}
	
	/*
	 * actualiza la torre c 
	 */
	private void presentarTorreC() {
		//borra todas las filas de la tabla
		((DefaultTableModel)TorreC.getModel()).setRowCount(0);
		
		//asigna todas las filas a la tabla
		modeloTablaTorreC.setRowCount(10);
		
		Nodo k;
		
		// para empezar a dibujar los discos al fnal y no al principio
		int rowDisco=(10-torreCPila.getContNodo());
		
		
		if(torreCPila.getContNodo()>0) {
			//itera los nodos de la pila
			for(k = torreCPila.getCabeza();k.getAbajo() != null; k=k.getAbajo()) {
				String[] normal={k.getDato()};
				modeloTablaTorreC.insertRow(rowDisco, normal);
				rowDisco++;
			}
			//setea el ultimo nodo
			if(k.getAbajo()==null) {
				String[] normal={k.getDato()};
				modeloTablaTorreC.insertRow(rowDisco, normal);
			}
		}
		//actualiza la intefaz grafica
		TorreC.setModel(modeloTablaTorreC);
		modeloTablaTorreC.setRowCount(10);
		
	}
	
	/*
	 * actualiza la torre b 
	 */
	private void presentarTorreB() {
		//borra todas las filas de la tabla
		((DefaultTableModel)TorreB.getModel()).setRowCount(0);
		
		//asigna todas las filas a la tabla
		modeloTablaTorreB.setRowCount(10);
		
		Nodo k;
		
		// para empezar a dibujar los discos al fnal y no al principio
		int rowDisco=(10-torreBPila.getContNodo());
		
		//itera los nodos de la pila
		if(torreBPila.getContNodo()>0) {
			for(k = torreBPila.getCabeza();k.getAbajo() != null; k=k.getAbajo()) {
				String[] normal={k.getDato()};
				modeloTablaTorreB.insertRow(rowDisco, normal);
				rowDisco++;
			}
			//setea el ultimo nodo
			if(k.getAbajo()==null) {
				String[] normal={k.getDato()};
				modeloTablaTorreB.insertRow(rowDisco, normal);
			}
		}
		//actualiza la intefaz grafica
		TorreB.setModel(modeloTablaTorreB);
		modeloTablaTorreB.setRowCount(10);
		
	}
	
	/*
	 * actualiza la torre A
	 */
	private void presentarTorreA() {
		//borra todas las filas de la tabla
		((DefaultTableModel)TorreA.getModel()).setRowCount(0);
		
		//asigna todas las filas a la tabla
		modeloTablaTorreA.setRowCount(10);
		
		Nodo k;
		
		// para empezar a dibujar los discos al fnal y no al principio
		int rowDisco=(10-torreAPila.getContNodo());
		
		//itera los nodos de la pila
		if(torreAPila.getContNodo()>0) {
			for(k = torreAPila.getCabeza();k.getAbajo() != null; k=k.getAbajo()) {
				String[] normal={k.getDato()};
				modeloTablaTorreA.insertRow(rowDisco, normal);
				rowDisco++;
			}
			//setea el ultimo nodo
			if(k.getAbajo()==null) {
				String[] normal={k.getDato()};
				modeloTablaTorreA.insertRow(rowDisco, normal);
			}
		}
		//actualiza la intefaz grafica
		TorreA.setModel(modeloTablaTorreA);
		modeloTablaTorreA.setRowCount(10);
		
	}
	/*
	 * mueve el nodo superior de la torre A a la torre B 
	 * solo si se cumple la condicion: nodo de b>nodo de A
	 */
	private void moverDeA_B() {
		try {
			// si esta vacia no hace nada
			if(torreAPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreAPila.peek());
				
				if (torreBPila.getContNodo()>0) {
					
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreBPila.peek())>0) {
						return;
					}
				}
				torreAPila.pop();
				torreBPila.push(plataforma);
				
				presentarTorreA();
				presentarTorreB();
				presentarCantidadMovimientos();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/*
	 * mueve el nodo superior de la torre A a la torre C 
	 * solo si se cumple la condicion: nodo de C>nodo de A
	 */
	private void moverDeA_C() {
		try {
			if(torreAPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreAPila.peek());
				
				if (torreCPila.getContNodo()>0) {
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreCPila.peek())>0) {
						return;
					}
				}
				torreAPila.pop();
				torreCPila.push(plataforma);
				
				presentarTorreA();
				presentarTorreC();
				presentarCantidadMovimientos();
				
				//Testea si el usuario ya gano
				if(torreCPila.getContNodo()== objetivo && contNumMov == numMinDeMovimientos) {
					JOptionPane.showMessageDialog(null, "felicidades haz alcanzado la cantidad minima de movimientos \n\n intenta con otro nivel de dificultad", "felicitaciones ", JOptionPane.WARNING_MESSAGE);
					
				}
				else if (torreCPila.getContNodo()== objetivo && contNumMov != numMinDeMovimientos) {
					JOptionPane.showMessageDialog(null, "felicidades haz ganado \n\n intenta superar el objetivo de movimientos minimos", "felicitaciones ", JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * mueve el nodo superior de la torre B a la torre A 
	 * solo si se cumple la condicion: nodo de A>nodo de B
	 */
	private void moverDeB_A() {
		try {
			if(torreBPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreBPila.peek());
				
				if (torreAPila.getContNodo()>0) {
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreAPila.peek())>0) {
						return;
					}
				}
				torreBPila.pop();
				torreAPila.push(plataforma);
				
				presentarTorreA();
				presentarTorreB();
				presentarCantidadMovimientos();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * mueve el nodo superior de la torre B a la torre C 
	 * solo si se cumple la condicion: nodo de C>nodo de B
	 */
	private void moverDeB_C() {
		try {
			if(torreBPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreBPila.peek());
				
				if (torreCPila.getContNodo()>0) {
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreCPila.peek())>0) {
						return;
					}
				}
				torreBPila.pop();
				torreCPila.push(plataforma);
				
				presentarTorreB();
				presentarTorreC();
				presentarCantidadMovimientos();
				
				//Testea si gano
				if(torreCPila.getContNodo()== objetivo && contNumMov == numMinDeMovimientos) {
					JOptionPane.showMessageDialog(null, "felicidades haz alcanzado la cantidad minima de movimientos \n\n intenta con otro nivel de dificultad", "felicitaciones ", JOptionPane.WARNING_MESSAGE);
					
				}
				else if (torreCPila.getContNodo()== objetivo && contNumMov != numMinDeMovimientos) {
					JOptionPane.showMessageDialog(null, "felicidades haz ganado \n\n intenta superar el objetivo de movimientos minimos", "felicitaciones ", JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/*
	 * mueve el nodo superior de la torre C a la torre A
	 * solo si se cumple la condicion: nodo de A>nodo de C
	 */
	private void moverDeC_A() {
		try {
			if(torreCPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreCPila.peek());
				
				if (torreAPila.getContNodo()>0) {
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreAPila.peek())>0) {
						return;
					}
				}
				torreCPila.pop();
				torreAPila.push(plataforma);
				
				presentarTorreA();
				presentarTorreC();
				presentarCantidadMovimientos();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/*
	 * mueve el nodo superior de la torre C a la torre B
	 * solo si se cumple la condicion: nodo de B>nodo de C
	 */
	private void moverDeC_B() {
		try {
			if(torreCPila.getContNodo()>0) {
				Nodo plataforma = new Nodo(torreCPila.peek());
				
				if (torreBPila.getContNodo()>0) {
					//Compara los lenght si es mayor a 0 significa q es mas grande, entonces no se puede mover
					if(plataforma.getDato().compareTo(torreBPila.peek())>0) {
						return;
					}
				}
				torreCPila.pop();
				torreBPila.push(plataforma);
				
				presentarTorreB();
				presentarTorreC();
				presentarCantidadMovimientos();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
     * Inicializa los componentes de la ventana, configura el diseño y 
     * prepara los modelos de las tablas.
     * todo realizado con windowbuilder
     */
	public VentanaPrincipalHanoi() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 445);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setFocusTraversalKeysEnabled(false);
		scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setFocusable(false);
		scrollPane.setName("Torre A");
		scrollPane.setBounds(24, 0, 116, 170);
		contentPane.add(scrollPane);
		
		TorreA = new JTable();
		TorreA.setFocusable(false);
		TorreA.setShowVerticalLines(false);
		TorreA.setShowHorizontalLines(false);
		TorreA.setShowGrid(false);
		TorreA.setRowSelectionAllowed(false);
		scrollPane.setViewportView(TorreA);
		TorreA.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Torre A"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		TorreA.getColumnModel().getColumn(0).setResizable(false);
		TorreA.setName("TorreA");
		TorreA.setBorder(new LineBorder(new Color(0, 0, 0)));
		TorreA.setToolTipText("");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setFocusTraversalKeysEnabled(false);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setFocusable(false);
		scrollPane_1.setName("Torre B");
		scrollPane_1.setBounds(167, 0, 102, 170);
		contentPane.add(scrollPane_1);
		
		TorreB = new JTable();
		TorreB.setRowSelectionAllowed(false);
		TorreB.setShowVerticalLines(false);
		TorreB.setShowHorizontalLines(false);
		TorreB.setShowGrid(false);
		TorreB.setName("TorreB");
		TorreB.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Torre B"
			}
		));
		scrollPane_1.setViewportView(TorreB);
		TorreB.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setFocusTraversalKeysEnabled(false);
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_2.setFocusable(false);
		scrollPane_2.setName("Torre C");
		scrollPane_2.setBounds(313, 0, 104, 170);
		contentPane.add(scrollPane_2);
		
		TorreC = new JTable();
		TorreC.setShowVerticalLines(false);
		TorreC.setShowHorizontalLines(false);
		TorreC.setShowGrid(false);
		TorreC.setRowSelectionAllowed(false);
		TorreC.setName("TorreC");
		TorreC.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Torre C"
			}
		));
		scrollPane_2.setViewportView(TorreC);
		TorreC.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton BtnA_B = new JButton("B");
		BtnA_B.setName("BtnA_B");
		BtnA_B.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeA_B();
			}
		});
		BtnA_B.setBounds(24, 181, 47, 23);
		contentPane.add(BtnA_B);
		
		JButton BtnA_C = new JButton("C");
		BtnA_C.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeA_C();
			}
		});
		BtnA_C.setName("BtnA_C");
		BtnA_C.setBounds(76, 181, 47, 23);
		contentPane.add(BtnA_C);
		
		BtnB_A = new JButton("A");
		BtnB_A.setName("BtnB_A");
		BtnB_A.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeB_A();
			}
		});
		BtnB_A.setBounds(167, 181, 47, 23);
		contentPane.add(BtnB_A);
		
		BtnB_C = new JButton("C");
		BtnB_C.setName("BtnB_C");
		BtnB_C.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeB_C();
			}
		});
		BtnB_C.setBounds(227, 181, 47, 23);
		contentPane.add(BtnB_C);
		
		BtnC_A = new JButton("A");
		BtnC_A.setName("BtnC_A");
		BtnC_A.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeC_A();
			}
		});
		BtnC_A.setBounds(323, 181, 47, 23);
		contentPane.add(BtnC_A);
		
		BtnC_B = new JButton("B");
		BtnC_B.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moverDeC_B();
			}
		});
		BtnC_B.setName("BtnC_B");
		BtnC_B.setBounds(375, 181, 47, 23);
		contentPane.add(BtnC_B);
		
		comboBoxDiscos = new JComboBox();
		comboBoxDiscos.setName("CbNumDiscos");
		comboBoxDiscos.setBackground(new Color(192, 192, 192));
		comboBoxDiscos.setModel(new DefaultComboBoxModel(new String[] {"3", "4", "5", "6", "7", "8", "9", "10"}));
		comboBoxDiscos.setBounds(230, 209, 116, 17);
		contentPane.add(comboBoxDiscos);
		
		lblMinMovimientos = new JLabel("");
		lblMinMovimientos.setHorizontalAlignment(SwingConstants.CENTER);
		lblMinMovimientos.setName("LblMinDeMovimientos");
		lblMinMovimientos.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblMinMovimientos.setBackground(new Color(255, 255, 255));
		lblMinMovimientos.setBounds(230, 237, 116, 17);
		contentPane.add(lblMinMovimientos);
		
		lblNumMovimientos = new JLabel("");
		lblNumMovimientos.setForeground(new Color(128, 0, 0));
		lblNumMovimientos.setFont(new Font("Arial Black", Font.PLAIN, 11));
		lblNumMovimientos.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumMovimientos.setName("LblMovimientos");
		lblNumMovimientos.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblNumMovimientos.setBackground(Color.WHITE);
		lblNumMovimientos.setBounds(230, 265, 116, 17);
		contentPane.add(lblNumMovimientos);
		
		JLabel lblMinimoDeMovimientos = new JLabel("número minimo de movimientos");
		lblMinimoDeMovimientos.setBorder(null);
		lblMinimoDeMovimientos.setBackground(Color.WHITE);
		lblMinimoDeMovimientos.setBounds(44, 237, 173, 17);
		contentPane.add(lblMinimoDeMovimientos);
		
		JLabel lblNmeroDeDiscos = new JLabel("número de discos");
		lblNmeroDeDiscos.setBorder(null);
		lblNmeroDeDiscos.setBackground(Color.WHITE);
		lblNmeroDeDiscos.setBounds(44, 210, 173, 17);
		contentPane.add(lblNmeroDeDiscos);
		
		JLabel lblNmeroDeMovimientos = new JLabel("número de movimientos");
		lblNmeroDeMovimientos.setBorder(null);
		lblNmeroDeMovimientos.setBackground(Color.WHITE);
		lblNmeroDeMovimientos.setBounds(44, 265, 173, 17);
		contentPane.add(lblNmeroDeMovimientos);
		
		JButton btnIniciar = new JButton("iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contNumMov=0;
				inciar();
					}
		});
		btnIniciar.setBounds(44, 315, 93, 39);
		contentPane.add(btnIniciar);
		
		JButton btnReiniciar = new JButton("reiniciar");
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reiniciar();
			}
		});
		btnReiniciar.setBounds(167, 315, 93, 39);
		contentPane.add(btnReiniciar);
		
		JButton btnResolver = new JButton("resolver");
		btnResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!lblMinimoDeMovimientos.getText().equals("") && torreCPila.getContNodo()!=objetivo);
				reiniciar();
				stop=false;
				resolverHanoi(objetivo, torreAPila, torreBPila, torreCPila);
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
				
				
			}
		});
		btnResolver.setBounds(292, 315, 93, 39);
		contentPane.add(btnResolver);
		
	
		modeloTablaTorreA = (DefaultTableModel) TorreA.getModel();
		modeloTablaTorreA.setRowCount(10);
		TorreA.setRowHeight(15);

		modeloTablaTorreB = (DefaultTableModel) TorreB.getModel();
		modeloTablaTorreB.setRowCount(10);
		TorreB.setRowHeight(15);

		modeloTablaTorreC = (DefaultTableModel) TorreC.getModel();
		modeloTablaTorreC.setRowCount(10);
		TorreC.setRowHeight(15);
		
		
		DefaultTableCellRenderer renderA= new DefaultTableCellRenderer();
		renderA.setHorizontalAlignment(SwingConstants.CENTER);
		TorreA.getColumnModel().getColumn(0).setCellRenderer(renderA);
		
		DefaultTableCellRenderer renderB= new DefaultTableCellRenderer();
		renderB.setHorizontalAlignment(SwingConstants.CENTER);
		TorreB.getColumnModel().getColumn(0).setCellRenderer(renderB);
		
		DefaultTableCellRenderer renderC= new DefaultTableCellRenderer();
		renderC.setHorizontalAlignment(SwingConstants.CENTER);
		TorreC.getColumnModel().getColumn(0).setCellRenderer(renderC);
		
		
		

	}
	
}

