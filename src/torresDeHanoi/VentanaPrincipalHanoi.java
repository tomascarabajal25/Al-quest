package torresDeHanoi;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/**
 * Clase que representa la interfaz gráfica principal para el juego de las Torres de Hanoi.
 * hereda jframe para la interacción con las pilas y las tablas (JTable).
 * @author Compiladores
 * @version 1.0
 */
public class VentanaPrincipalHanoi extends JFrame {
	private static final long serialVersionUID = 1L;
	int objetivo=0;
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
	private JComboBox<String> comboBoxDiscos;
    private ControllerHanoi controller;
    private boolean juegoIniciado=false;
    DefaultTableModel modeloTablaTorreA, modeloTablaTorreB,modeloTablaTorreC ;

    /**
     * Inicializa los componentes de la ventana, configura el diseño y 
     * prepara los modelos de las tablas.
     * todo realizado con windowbuilder
     */
    public VentanaPrincipalHanoi() {
        setTitle("Hanoi");
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
				if (getJuegoIniciado()
						) {
					controller.moverA_B();
				}
			}
		});
		BtnA_B.setBounds(24, 181, 47, 23);
		contentPane.add(BtnA_B);
		
		JButton BtnA_C = new JButton("C");
		BtnA_C.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					controller.moverA_C();
				}
			}
		});
		BtnA_C.setName("BtnA_C");
		BtnA_C.setBounds(76, 181, 47, 23);
		contentPane.add(BtnA_C);
		
		BtnB_A = new JButton("A");
		BtnB_A.setName("BtnB_A");
		BtnB_A.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					controller.moverB_A();
				}
			}
		});
		BtnB_A.setBounds(167, 181, 47, 23);
		contentPane.add(BtnB_A);
		
		BtnB_C = new JButton("C");
		BtnB_C.setName("BtnB_C");
		BtnB_C.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					controller.moverB_C();
				}
			}
		});
		BtnB_C.setBounds(227, 181, 47, 23);
		contentPane.add(BtnB_C);
		
		BtnC_A = new JButton("A");
		BtnC_A.setName("BtnC_A");
		BtnC_A.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					controller.moverC_A();
				}
			}
		});
		BtnC_A.setBounds(323, 181, 47, 23);
		contentPane.add(BtnC_A);
		
		BtnC_B = new JButton("B");
		BtnC_B.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					controller.moverC_B();
				}
			}
		});
		BtnC_B.setName("BtnC_B");
		BtnC_B.setBounds(375, 181, 47, 23);
		contentPane.add(BtnC_B);
		
		comboBoxDiscos = new JComboBox<>();
		comboBoxDiscos.setName("CbNumDiscos");
		comboBoxDiscos.setBackground(new Color(192, 192, 192));
		comboBoxDiscos.setModel(new DefaultComboBoxModel<>(new String[] {"3", "4", "5", "6", "7", "8", "9", "10"}));
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
				iniciar();
				setJuegoIniciado(true);
		}});
		btnIniciar.setBounds(44, 315, 93, 39);
		contentPane.add(btnIniciar);
		
		JButton btnReiniciar = new JButton("reiniciar");
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getJuegoIniciado()) {
					reiniciar();
				}
			}
		});
		btnReiniciar.setBounds(167, 315, 93, 39);
		contentPane.add(btnReiniciar);
		
		JButton btnResolver = new JButton("resolver");
		btnResolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJuegoIniciado()) {
					try {
					controller.resolver();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
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
  //METODOS DE COMPORTAMIENTO------------------------------------------------------
    /*
	 * reinicia el contador de movimientos a 0 y vuelve todos los discos a la torre A
	 */
    private void reiniciar() {
    	controller.reiniciar(objetivo);	
	} 
    
    /*
     * inicia el juego con la cantidad de discos establecidos
     */
    private void iniciar() {
    	objetivo= Integer.parseInt(comboBoxDiscos.getSelectedItem().toString());
		controller = new ControllerHanoi(objetivo,this);
	}
    
    /*
     * actualiza la vista de las torres y la cantidad de movimientos
     */
    public void actualizar(EstadoHanoi estado) {
        presentarTorre(TorreA, estado.torreA);
        presentarTorre(TorreB, estado.torreB);
        presentarTorre(TorreC, estado.torreC);

        lblNumMovimientos.setText(String.valueOf(estado.movimientos));
        lblMinMovimientos.setText(String.valueOf(estado.minMovimientos));
    }
    
    /**
     * Metodo unificado para actualizar cualquier torre.
     * 
     * PRE:
     * - tabla != null
     * - discos != null
     * 
     * POST:
     * - La tabla refleja el estado de la torre.
     */
    private void presentarTorre(JTable tabla, String[] discos) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        modelo.setRowCount(0);
        modelo.setRowCount(10);

        int row = 10 - contar(discos);

        for (String d : discos) {
            if (d != null) {
                modelo.setValueAt(d, row++, 0);
            }
        }
    }
    /*
     * cuenta la cantidad de discos en el array
     */
    private int contar(String[] discos) {
        int c = 0;
        for (String d : discos) if (d != null) c++;
        return c;
    }
	
	//lanza la ventana que pregunta al usuario si quiere seguir con la resolucion recursiva
	public boolean preguntarContinuar(int paso) {
	    int opt = JOptionPane.showConfirmDialog(
	        this,
	        "Paso #" + paso + "\n¿Desea continuar?",
	        "Resolucionas",
	        JOptionPane.YES_NO_OPTION
	    );
	    return opt == JOptionPane.YES_OPTION;
	}
	/*
	 * muestra la alerta de victoria
	 */
	public void mostrarVictoria() {
		JOptionPane.showMessageDialog(null, "felicidades haz ganado \n\n intenta superar el objetivo de movimientos minimos"
				, "felicitaciones "
				, JOptionPane.WARNING_MESSAGE);
	}
	/*
	 * muestra la alerta de victoria con el minimo de movimientos
	 */
	public void mostrarVictoriaPerfecta() {
		JOptionPane.showMessageDialog(null, "felicidades haz alcanzado la cantidad minima de movimientos \n\n intenta con otro nivel de dificultad",
				"felicitaciones ",
				JOptionPane.WARNING_MESSAGE);
	}
	
	//GETTER SIMPLES-----------------------------------------------------------------
	//retorna si el juego fue iniciado o no
	public boolean getJuegoIniciado() {
		return this.juegoIniciado;
	}
	
	//SETTERS SIMPLES---------------------------------------------------------------
	// cambia el estado del juego iniciado
	private void setJuegoIniciado(boolean estado) {
		this.juegoIniciado=estado;
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
}