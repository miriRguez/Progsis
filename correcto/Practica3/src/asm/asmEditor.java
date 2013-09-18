/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import sun.security.jgss.TokenTracker;

/**
 *
 * @author Miriam
 */
public class asmEditor extends javax.swing.JFrame {
    /*
     * el archivo que contendrá la biblioteca de palabras reservadas
     */
    private List<String> reservadas = new ArrayList();
    /*
     * JFileChooser no servirá para abrir los archivos
     * es una interface gráfica clasica par abrir archivos
     */
    private JFileChooser fOpen;
   /*
    *FileReader es un  InputStreamReader -> FileInputStream
    * que nos sirve para abrir archivos que contine caracteres o texto plano
    */
    private FileReader archivoleido;
    /*
     *FileNameExtensionFilter nos sirve para filtrar los archivos
     * se le pasa como parametro  al metodo setFileFilter(...) de JFileChooser
     * para que este solo pueda abrir archivos con X o Y extención
     */
    private FileNameExtensionFilter filtro;
    /*
     * trabajamos sobre caracteres asi que para manejar el streem de caracteres 
     * "texto de las lineas de codigo" bufelizamos el FileReader con un BufferedReader
     */
    private BufferedReader buffer;
    /*
     * File es la clase que nos sirve para manejar archivos y directorios
     * JFileChooser tiene el metodo getSelectedFile() que nos retorna 
     * un objeto File el cual en este caso será el archivo con el codigo fuente
     */
    private File f;// clase File para manejar el archivo
    /*ArrayList que contendrá las lineas del archivo fuente
    * se recorre el rchivo linea por linea agregndo un elemento al ArrayList
    * hasta que ya no hay mas lineas...
    */
    private ArrayList<String> lineas;
    
    
    private HashMap<String, String> tabop;// la key sera la palabra clave 
    //y el objeto contenido las instruciones
    
    /*
     * opcional una variable String que contendrá el codigo html
     * para mostrar o setearle al JEditorPane() que 
     * mostrará en un futuro los resultados de modo grafico 
     * en codigo html 
     */
    private String html;
    private boolean bandera2 = false;

    public asmEditor() {
        /*
         * inicia los elementos o componentes que netbeans genera al usar el Design
         * como los JComponents de la GUI de la plicaciones y otros valores 
         * por defecto, este codigo se genera automaticamente...
         */
        initComponents();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//cerramos la appp
                                                            //cuando precionemos cerrar
        this.setSize(800,400);// un tamaño X Y para el frame
        this.setResizable(false);// qu no se cambie de tamaño el frame
        htmlOut.setContentType("text/html");// le seteamos el tipo de contenido 
                                            //al htmlOut qu es un JEditorPane
                                            // de modo que al poner setText(html)
                                            // html se muestre coo codigo HTML 
        htmlOut.setText("<span>"
                + "Para hacer que el programa funcione primero elegir en el menu TABOP la ruta donde se encuentra el archivo TABOP y posteriormente desde el meno archivo seleccionamos el archivo a anlizar, el resultado se mostrara en cabina</span><br>");

        ///completar la "biblioteca" de palabras reservadas
        reservadas.add("MOV");reservadas.add("XCHG"); reservadas.add("LDX"); reservadas.add("CLC");reservadas.add("ADCA");reservadas.add("ADBC");
        reservadas.add("ADDA");reservadas.add("ADDB");reservadas.add("ADDD");reservadas.add("ANDA");reservadas.add("ANDB");
        reservadas.add("MOV"); reservadas.add("LDAA"); reservadas.add("ABX");
        reservadas.add("ADCA");reservadas.add("ADCB");reservadas.add("ADDA");reservadas.add("ADDB");reservadas.add("ADD");reservadas.add("ANDA");reservadas.add("ANDCC");
        reservadas.add("ASL");reservadas.add("ASLA");reservadas.add("ASLD");reservadas.add("ASR");
        reservadas.add("ASRA");reservadas.add("ASRB");reservadas.add("BCLR");reservadas.add("BCS");reservadas.add("BEQ");reservadas.add("BGE");reservadas.add("BGND");
        reservadas.add("BGT");reservadas.add("BHI");reservadas.add("BHS");reservadas.add("BITA");reservadas.add("BITB");reservadas.add("BLE");
        reservadas.add("BLO");reservadas.add("BLS");reservadas.add("BLT");reservadas.add("BMI");reservadas.add("BNE");reservadas.add("BPL");reservadas.add("BRA");
        reservadas.add("BRCLR");reservadas.add("BRN");reservadas.add("BRSET");reservadas.add("BSET");reservadas.add("BSR");reservadas.add("BVC");reservadas.add("BVS");reservadas.add("CALL");
        reservadas.add("CBA");reservadas.add("CLC");reservadas.add("CLI");reservadas.add("CLR");reservadas.add("CLRA");reservadas.add("CLRB");reservadas.add("CLV");reservadas.add("CMPA");
        reservadas.add("CMPB");reservadas.add("COM");reservadas.add("COMA");reservadas.add("COMB");reservadas.add("CPD");reservadas.add("CPS");reservadas.add("CPX");
        reservadas.add("CPY");reservadas.add("DAA");reservadas.add("DBEQ");reservadas.add("DBNE");reservadas.add("DEC");reservadas.add("DECA");reservadas.add("DECB");
        reservadas.add("DES");reservadas.add("DEX");reservadas.add("DEY");reservadas.add("EDIV");reservadas.add("EDIVS");reservadas.add("EMACS");reservadas.add("EMAXD");
        reservadas.add("EMAXM");reservadas.add("EMIND");reservadas.add("EMINM");reservadas.add("EMUL");reservadas.add("EMULS");reservadas.add("EORA");reservadas.add("EORB");
        reservadas.add("ETBL");reservadas.add("EXG");reservadas.add("FDIV");reservadas.add("IBEQ");reservadas.add("IBNE");reservadas.add("IDIV");reservadas.add("IDIVS");
        reservadas.add("INC");reservadas.add("INCA");reservadas.add("INCB");reservadas.add("INS");reservadas.add("INX");reservadas.add("INY");reservadas.add("JMP");reservadas.add("JSR");
        reservadas.add("LBCS");reservadas.add("LBEQ");reservadas.add("LBGE");reservadas.add("LBGT");reservadas.add("LBHI");reservadas.add("LBHS");reservadas.add("LBLE");
        reservadas.add("LBLO");reservadas.add("LBLS");reservadas.add("LBLT");reservadas.add("LBMI");reservadas.add("LBNE");reservadas.add("LBRN");reservadas.add("LBVC");reservadas.add("LBVS");
        reservadas.add("LDAA");reservadas.add("LDAB");reservadas.add("LDD");reservadas.add("LDS");reservadas.add("LDX");reservadas.add("LDY");reservadas.add("LEAS");reservadas.add("LEAX");reservadas.add("LEAY");
        reservadas.add("LSL");reservadas.add("LSLA");reservadas.add("LSLB");reservadas.add("LSLD");reservadas.add("LSR");
        reservadas.add("LSRA");reservadas.add("LSRB");reservadas.add("LSRD");reservadas.add("MAXA");reservadas.add("MAXM");reservadas.add("MEM");reservadas.add("MINA");reservadas.add("MINM");
        reservadas.add("MOVB");reservadas.add("MOVW");reservadas.add("MUL");reservadas.add("NEG");reservadas.add("NEGA");reservadas.add("NEGB");reservadas.add("NOP");reservadas.add("ORAA");
        reservadas.add("ORAB");reservadas.add("ORCC");reservadas.add("PSHA");reservadas.add("PSHB");reservadas.add("PSHC");reservadas.add("PSHD");reservadas.add("PSHX");reservadas.add("PSHY");
        reservadas.add("PULA");reservadas.add("PULB");reservadas.add("PULD");reservadas.add("PULX");reservadas.add("PULY");reservadas.add("REV");reservadas.add("REVW");reservadas.add("ROL");reservadas.add("ROLA");
        reservadas.add("ROLB");reservadas.add("ROR");reservadas.add("RORA");reservadas.add("RORB");reservadas.add("RTC");reservadas.add("RTI");reservadas.add("RTS");reservadas.add("SBA");reservadas.add("SBCA");reservadas.add("SBCB");
        reservadas.add("SEC");reservadas.add("SEI");reservadas.add("SEV");reservadas.add("SEX");reservadas.add("STAA");reservadas.add("STAB");reservadas.add("STD");reservadas.add("STOP");reservadas.add("STS");
        reservadas.add("STX");reservadas.add("STY");reservadas.add("SUBA");reservadas.add("SUBB");reservadas.add("SUBD");reservadas.add("SWI");
        reservadas.add("TAB");reservadas.add("TAP");reservadas.add("TBA");reservadas.add("TBEQ");reservadas.add("TBL");reservadas.add("TBNE");reservadas.add("TFR");reservadas.add("TPA");reservadas.add("TRAP");reservadas.add("TST");
        reservadas.add("TST");reservadas.add("TSTA");reservadas.add("TSTB"); reservadas.add("TSX"); reservadas.add("TSY"); reservadas.add("TXS"); reservadas.add("TYS");reservadas.add("WAI");
        reservadas.add("WAV");reservadas.add("WAVR");reservadas.add("XGDX");reservadas.add("XGDY");
        Collections.sort(reservadas);
        // las ordenamos para que la busqueda de 
        //coincidencias sea más rápida...
    }
    

//codigo gnerado por netbeans...[por explicar en su momento si es que hay dudas...]    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem3 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        htmlOut = new javax.swing.JEditorPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        configMenu = new javax.swing.JMenu();
        TABOP = new javax.swing.JMenuItem();

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(htmlOut);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        configMenu.setText("TABOP");

        TABOP.setText("Selec. Archivo");
        TABOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TABOPActionPerformed(evt);
            }
        });
        configMenu.add(TABOP);

        jMenuBar1.add(configMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //netbeas te crea metodos para las acciones de los emementos 
    //que genera automaticamente como los ActionListener, estos metodos son 
    //agregados o llamados en el codigo que se genera automatico en initComponents()
    //en este caso, esta es la acción del jMenuItem1 que corresponde al item open 
    //del menu....
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if(bandera2){
        openASM();// llama a la fucion openASM que basicamente hace todo el proceso..
        }else{
            JOptionPane.showMessageDialog(rootPane, "debes crgar a TABOP primero");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    //action para el boton o item menu salir
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);// un simple exit(0)
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void TABOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TABOPActionPerformed
       openTABOP();
    }//GEN-LAST:event_TABOPActionPerformed
    
    public void openTABOP(){
        filtro = new FileNameExtensionFilter("txt and asm", "txt","tabop");
       fOpen = new JFileChooser();
       fOpen.setFileFilter(filtro);
       
       int i = fOpen.showOpenDialog(this);// abre la interface gafica del 
                                            //file chooser nos retorna un codigo de error 
        if(i == JFileChooser.APPROVE_OPTION){// si todo esta bien comensamos con el archivo
                        //f es nuestro File que contendrá la ubicación e información del archivo abierto
            f = fOpen.getSelectedFile();// este metodo nos regrea el File del archibo que abrimos con el
                                        //JFileChooser
            try {
                archivoleido = new FileReader(f);// es InputStreamReader -> FileInputStream 
                                               // el constructor nos pide la ruta o un File del archivo
                                // en este caso hemos puesto f que es el archivo abierto con el JFileChooser
               buffer = new BufferedReader(archivoleido);// buferizamos el streem del archivo
               String leer = buffer.readLine();// tipoco algoritmo usamos un String para ig guardando linea por line
                
               tabop = new HashMap();// inici
                String[] tokens;
                String llave="";
                String manual = null;
                boolean bandera = false;
                
                
               while(leer != null){// mientras hay lineas en el buber
                   if(!leer.equals("*")){
                       if(bandera == false){
                           tokens = leer.split(" ");
                           
                           llave = tokens[0];
                           if(tokens[0]!=null){
                           bandera = true;
                           }
                       }
                       manual += leer+ "\n";
                   }else{
                       tabop.put(llave, manual);
                       manual ="";
                       bandera = false;
                   }
                   leer = buffer.readLine();
               }
               bandera2 = true;
               JOptionPane.showMessageDialog(rootPane, "y puedes cargar el ASM");
                //System.out.println(tabop);
              //  System.out.println(tabop.get("LDX"));
                
               
               
               
               
            } catch (FileNotFoundException ex) {
                Logger.getLogger(asmEditor.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "No tiene permisos para abrir el archivo");
            } 

            
            
 
            
               
    }
    }
    
    public void openASM(){
        html ="";
        filtro = new FileNameExtensionFilter("txt and asm", "txt","asm");// creamos el filtro para txt y asm
        fOpen = new JFileChooser();//  creamos el JFileChooser
        fOpen.setFileFilter(filtro);// le seteamos el filtro para que solo busque archivos asm y txt 
        int i = fOpen.showOpenDialog(this);// abre la interface gafica del 
                                            //file chooser nos retorna un codigo de error 
        if(i == JFileChooser.APPROVE_OPTION){// si todo esta bien comensamos con el archivo
                        //f es nuestro File que contendrá la ubicación e información del archivo abierto
            f = fOpen.getSelectedFile();// este metodo nos regrea el File del archibo que abrimos con el
                                        //JFileChooser
            try {
               archivoleido = new FileReader(f);// es InputStreamReader -> FileInputStream 
                                // el constructor nos pide la ruta o un File del archivo
                                // en este caso hemos puesto f que es el archivo abierto con el JFileChooser
               buffer = new BufferedReader(archivoleido);// buferizamos el streem del archivo
               String leer = buffer.readLine();// tipoco algoritmo usamos un String para ig guardando linea por line
               lineas = new ArrayList();// inicializamos el Array list que contendrá cada linea por analizar
               while(leer != null){// mientras hay lineas en el buber
                   lineas.add(leer);// agregamos al ArrayListe cada linea leida
                   leer = buffer.readLine();// asignamos a leer la siguiene linea
               }
               
             Iterator<String> lineasI = lineas.listIterator();/// con el iterator iteramos el arraylist que contiene las lineas
             
///////////////////////////////////////////////////recoremos todo el archivo linea por linea.....             
             while(lineasI.hasNext()){/// mientas el iterator tenga lineas por leer las leemos y hacemos lo que venga..
                 String comentarios= "";// variable para comentarios
                 String codigo="";// variable pata too el codigo completo
                 String etiqueta ="";/// variable para etiquetas
                 ArrayList <String> instruc = new ArrayList();// vatiable para instruccions
                 String operadores ="";
                 
                 String linnn[];// array de Strings que contrenrá 2 strins uno para el codifo completo y otro para los comentarios
                 linnn = divideComentario(lineasI.next());// llamamos a divide que esta funcion nos separa el comentario del codigo

                 comentarios = linnn[0];// comentarios en la pocicion 0
                 codigo = linnn[1];// el resto del codigo en a continuar analizando
                 
                 if(codigo.length()>0){// si hay codigo lo analizamos
                     
                 /// checamos si se inicializa con posible etiqueta o palabra recervada...
                 if((codigo.charAt(0)!=' ') && Character.isAlphabetic(codigo.charAt(0)) || (codigo.charAt(0)=='_')){

                     String tokens[] = codigo.split("[ \t]");// hacemos un token con espacios
                     
                     if(tokens.length>0){//si hay tokens continuamos con el analicis
                         ///JOptionPane.showMessageDialog(rootPane, tokens[0]);
                         if(!esReservada(tokens[0])){// si no es un palabra recervada entonces token[0] es una etiqueta
                            etiqueta = tokens[0];
                           
                            int x=1;
                            if(x<tokens.length){//si existen mas de 1 token
                                
                            while(tokens[x].equals("")){// recoremos la linea de codigo hasta que no tengamos mas espacio
                                x++;// agregamos ++ x hasta encontrar un caracter noespacio
                            }
                            
                            for(int j = x;j<tokens.length; j++){// metemos el resto del codigo en operadores pana analizarlos...
                              if(!esReservada(tokens[j]))// sino es palabra recerbada la ponemos en operadores  
                                operadores+=tokens[j];
                              else//si es palabra reservada en instruc
                                instruc.add (tokens[j]);  
                            }
                            
                            }
                        }else{
                            //como ya sabemos que no hay espacios en el inicio y que por defectos es una palabra recervada
                            // entonces por defecto tokens[0] es una instruccion
                            instruc.add(tokens[0]); 
                            
                            int x=1;
                            if(x<tokens.length){
                                while(tokens[x].equals("")){// recoremos la linea de codigo hasta que no tengamos mas espacio
                                    x++;// agregamos ++ x hasta encontrar un caracter noespacio
                                }
                                for(int j = x;j<tokens.length; j++){// metemos el resto del codigo en operadores pana analizarlos...
                                  if(!esReservada(tokens[j]))// sino es palabra recerbada la ponemos en operadores  
                                    operadores+=tokens[j];
                                  else//si es palabra reservada en instruc
                                    instruc.add(tokens[j]);
                                }
                            }
                        }
                     }
                     
                 }else{// si no iniciacon una posible etiqueta entonces analizamos el codigo sin tomar encuenta una posible etiqueta...
                     String tokens[] = codigo.split("[ \t]");// hacemos el split  
                     
                     if(tokens.length>0){// si hay tokens continuamos con el analicis 
                         
                         // nos falta checar los espacios pues pueden existir tokens con espacios entonces tokens[0] podria ser un espacio
                         //pero lo que sigue de n espacios si podria ser una instrucción, terminamos de implementar la busqueda de palabras recervadas 
                         // contemplando los espacios
                        int x = 0;// index de los tokens
                        while(tokens[x].equals("")){// recoremos la linea de codigo hasta que no tengamos mas espacio
                            x++;// agregamos ++ x hasta encontrar un caracter noespacio
                        }
                        for(int j = x;j<tokens.length; j++){// metemos el resto del codigo en operadores pana analizarlos...
                            if(!esReservada(tokens[j].toUpperCase()))// sino es palabra recerbada la ponemos en operadores  
                                operadores+=tokens[j];
                            else//si es palabra reservada en instruc
                                instruc.add(tokens[j]); 
                        }

                    }
                 
                 }
             }// no hay codigo por lotanto solo hay comentarios o no hay nada
                 
                 // imprimimos tdas las variables en consola
                    /*System.out.println("Etiqueta: "+etiqueta);
                    */
                 html = "";
                 for (int j = 0; j < instruc.size(); j++) {
                      System.out.println("instruccion "+(j+1)+": " +instruc.get(j));
                      String man=tabop.get(instruc.get(j).toUpperCase());
                      
                      if(!(man == null)){
                      System.out.println(tabop.get(instruc.get(j).toUpperCase()));
                      }else{
                          System.out.println("La instrucion  "+instruc.get(j)+" no es vlida");
                      }
                 }
                 
                 for (int j = 0; j < instruc.size(); j++) {
                      html+="<span>instruccion "+(j+1)+": " +instruc.get(j)+"</span><br>";
                      String man=tabop.get(instruc.get(j).toUpperCase());
                      if(!(man == null)){
                          String mans[] = man.split("\n");
                          for (String string : mans) {
                             html+="<span> "+string+"</span><br>"; 
                          }
                      }else{
                          html+="<span>La instrucion "+instruc.get(j)+" no es vlida </span><br>";
                      }
                 }   
                    /*
                   String[] separaOp = operadores.split(",");
                 /*  for (int j = 0; j < separaOp.length; j++) {
                       System.out.println("Operador "+(j+1)+": "+separaOp[j]);///realmente contiene el codigo
                     
                 }*/
                    
                    //System.out.println("Comentario:" +comentarios);
                    System.out.println("-------------------------------------");
                //salida en GUI    
                   // html+="<span>Etiqueta: "+etiqueta+"</span><br>";
                    
               
                  /*  for (int j = 0; j < instruc.size(); j++) {
                   <span>instruccion"+(j+1)+": "+"<br>"+tabop.get(instruc.get(j).toUpperCase())+"</br>"+"</span><br>";
                    }*/
                   // for (int j = 0; j < separaOp.length; j++) {
                    //html+="<span>Operador "+(j+1)+": "+separaOp[j]+"</span><br>";
                    //}
                   // html+="<span>Comentario: "+comentarios+"</span><br>";
                    html+="<hr>";
                    // por validar después de la primera instrucción, es decir odria contener operadores
                    // u otra instrucción seguida de otros operadores
                    
           //  html+="<span>"+tabop.get(instruc.get(j).t)+"</span><br>";
                  
               }
                htmlOut.setText(html);  
             
               ///////////////////fin 
             
               archivoleido.close();//cerramos archivo (stream)
               buffer.close();// cerramos el bfer
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Archivo no encontrado");
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "No tiene permisos para abrir el archivo");
            } 
        }else{
            JOptionPane.showMessageDialog(rootPane, "Error no se puede abrir el archivor");
        }
    }
   
    /*nos retorna true si es palabra reservada*/
    public boolean esReservada(String palabra){
        return reservadas.contains(palabra.toUpperCase());
    }
    
    public boolean esOperador(String palabra){
        //if(String.)
    return false;
    }

   /*
    * dividimos el el string line que contine la linea completa
    * y la dibidimos en 2 strings que serán retornados
    * index 0 coresponde a los comentarios
    * index 1 a el codigo que no es comentario
    */ 
   public String[] divideComentario(String line){
       String comentario="", codigo="";//variables para comentario y codigo
       boolean escomentario = false;//auxiliar o bandera boolean para saber si hay
       //o no comentario
       
       for(int i=0;i <line.length();i++){//recorremos toda la linea
           if(line.charAt(i)==';'){/// cuando nos topemos con un ;
               //significa que a partir de alli ya todo es comentario
               escomentario = true;
           }
           /*
            * en cada iteración se agrega cada char de la linea a comentario una 
            * ves que se detecta que ya es comentario
            */
           if(escomentario){
               comentario+=line.charAt(i);
           }else{// mientras no se detecte que hay comentarios
           codigo+=line.charAt(i);// agregamso todo a codigo
           }
       }
       try{
           /*
            * sustraemos el ; del comentario en caso de que no hay mas nos podria dar un 
            * indexOutOfBou.... una excepción seria el caso de que el comentario 
            * fuera nulo es deci despues de ; ya no hay mas caracteres "comentario bacio"
            */
        comentario = comentario.substring(1);// si no hay errores pues basicamente
        //se pasa todo el comentario quitando el ;
       }catch(Exception e){
               comentario = "------";// si el comentario esta bacio pues ponemos en su lugar ------
       }
       /*
        * podria darce el caso de lineas sin codigo o saltos de linea sin codigo
        * esto generaria un "" en el string de comentarios dando como lugar 
        * un length == 0 en este caso ponemos mejor ----- es decir no hay comentarios...
        */
       if(comentario.length()==0)comentario = "------";
       
       // creamos nuestro arreglo result que contiene tanto los comentarios
       //como el codigo ya separados result[0] = comentarios
       //result[1]= codigo
       String[] result ={comentario, codigo};
       
       return result;// retornamos result
   }
  

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * todo este codigo se genera automaticamente 
         * es para standarizar el LookAndFeels
         * detectar errores si no encuentra el LookAndFeels descrito 
         * y en su defecto usar uno nativo, tambien nossirve para manejar
         * compatibilidad con otros OS, en si se encarga del LookAndFeels y 
         * evitar fallas no hay que explicar mucho por ahora... cuando tengamos tiempo
         * lo xplicamos...
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(asmEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(asmEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(asmEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(asmEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Existen cietos porbles con swing y los eventos 
         * hay algo en java que se le llama ola de eventos, cada vez que hay yn evento
         * estos se van a una cola de eventos y se atienden por prioridad
         * lo que hacemos aquí sin entrr en detalles es resolver siertos poblemas 
         * posibles al ejecutar el frame en otro hilo, invokeLater resive un runnable
         * y en su run creamos un objeto del frame asmEditor lo hacemos vidible,
         * se crea un nuevo hilo de ejecución y los eventos son tratados de manera 
         * adecuada, so solo es para evitar porblemas con los eventos de 
         * swing, que tealmente yo no he visto mucho porblema, pero asi te lo genera 
         * netbeans
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new asmEditor().setVisible(true);
            }
        });
    }
    ///todas estas variables son las que se van creando
    //en automaico y son los componentes de la GUI generada en Design
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem TABOP;
    private javax.swing.JMenu configMenu;
    private javax.swing.JEditorPane htmlOut;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
