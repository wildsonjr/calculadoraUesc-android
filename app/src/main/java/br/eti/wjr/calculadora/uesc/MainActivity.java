package br.eti.wjr.calculadora.uesc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Activity principal
 * 
 * @author Wildson Jr.
 * @version 1.1
 * @since 1.0
 */
public class MainActivity extends Activity {
    /**
     * Lista para armazenar os EditTexts utilizados no cálculo da situação.
     */
    private List<EditText> editTextList = new ArrayList<EditText>();
    
    /**
     * KeyListener que permite apenas a entrada de números (0-9) com casas decimais.
     */
    private DigitsKeyListener decimalKeyListener = new DigitsKeyListener(false, true);

    private TextView txvSituacao, txvInfo, txvMedia;
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * 
     * Método chamado quando o Activity é criado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// force activity doesn't have a title bar
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	// force portrait mode
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnAdicionarCredito = (Button) findViewById(R.id.btnAdicionarCredito);
        btnAdicionarCredito.setOnClickListener(btnAdicionarCreditoListener);
        
        Button btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(btnCalcularListener);

        Button btnAc = (Button) findViewById(R.id.btnAC);
        btnAc.setOnClickListener(btnAcListener);

        txvSituacao = (TextView) findViewById(R.id.txvSituacao);
        txvMedia = (TextView) findViewById(R.id.txvMedia);
        txvInfo = (TextView) findViewById(R.id.txvInfo);

        adicionarCredito();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Evento de click atribuído ao botão de adicionar crédito.
     */
    private OnClickListener btnAdicionarCreditoListener = new OnClickListener() {
        public void onClick(View view) {
            adicionarCredito();
        }
    };
    
    /**
     * Evento de click atribuído ao botão de remover crédito.
     */
    private OnClickListener btnRemoverCreditoListener = new OnClickListener() {
        public void onClick(View view) {
            removerCredito((LinearLayout) ((Button) view).getParent());
        }
    };

    /**
     * Evento de click atribuído ao botão de limpar tela (AC).
     */
    private OnClickListener btnAcListener = new OnClickListener() {
        public void onClick(View view) {
            txvInfo.setText("");
            txvMedia.setText("");
            txvSituacao.setText("");

            for (int i=0; i<editTextList.size(); i++){
                EditText editText = editTextList.get(i);
                editText.setText("");
            }
        }
    };
    
    /**
     * Evento de click atribuído ao botão de calcular a situação.
     */
    private OnClickListener btnCalcularListener = new OnClickListener() {
        public void onClick(View view) {
            double pontos     = 0;
            double media      = 0;
            double provaFinal = 0;
            
            for (EditText editText : editTextList) {
                try {
                    pontos += Double.valueOf(editText.getText().toString());
                } catch (NumberFormatException e) {
                    pontos += 0;
                }
            }
            
            media = pontos / (double) editTextList.size();
            
            if (media < 7) {
                provaFinal = ((50 - (media * 6)) / 4);
            }
            
            int situacao;
            int cor;
            
            if (provaFinal == 0) {
                situacao = R.string.aprovado;
                cor = Color.BLUE;
            } else if (provaFinal > 10) {
                situacao = R.string.reprovado;
                cor = Color.RED;
            } else {
                situacao = R.string.em_final;
                cor = Color.rgb(127, 0, 0);
            }

            txvSituacao.setText(getResources().getString(situacao));
//            textView.setTextColor(cor);

            txvMedia.setText(String.format(Locale.US, "%.2f", media));

            if(provaFinal<=10&&provaFinal>0){ //Se a nota para a final estiver entre 0 e 10 -- em final
                txvInfo.setText(String.format(getResources().getString(R.string.precisa_de), provaFinal));
            } else if(provaFinal>10){ //Se a nota para a final for maior que 10 -- reprovado
                txvInfo.setText(getResources().getString(R.string.nao_ha_como_passar));
            } else if(provaFinal==0){ //Se a nota para a final for igual a 0 -- aprovado
                txvInfo.setText(getResources().getString(R.string.parabens));
            }
        }
    };
    
    
    /**
     * Esse método adiciona um crédito ao layout.
     */
    private void adicionarCredito() {
        if (editTextList.size() == 10) {
            return;
        }
        
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llCreditos);
        linearLayout.addView(createRow());
    }
    
    /**
     * Esse método remove um crédito do layout.
     * 
     * @param row A TableRow que contém o crédito a ser removido.
     */
    private void removerCredito(LinearLayout row) {
        if (editTextList.size() == 1) {
            return;
        }
        
        EditText editText = (EditText) row.getChildAt(0);
        editTextList.remove(editText);
        
        LinearLayout linearLayout = (LinearLayout) row.getParent();
        linearLayout.removeView(row);
        
        atualizarCreditos();
    }
    
    /**
     * Esse método atualiza a id e a hint dos créditos. 
     */
    private void atualizarCreditos() {
        int id = 0;
        for (EditText editText : editTextList) {
            if (++id < editText.getId()) {
                editText.setId(id);
                editText.setHint(createHint(id));
            }
        }
    }
    
    
    /**
     * Método que cria um LinearLayout contendo um crédito e um botão de remoção.
     * 
     * @return O LinearLayout contendo o crédito e o botão de remoção.
     */
    private LinearLayout createRow() {
        LayoutParams lp1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4);
        LayoutParams lp2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.addView(createEditText(editTextList.size() + 1), lp1);
        linearLayout.addView(createButton(), lp2);
        
        return linearLayout;
    }
    
    /**
     * Método que cria um EditText equivalente a um crédito.
     * 
     * @param id O número do crédito.
     * @return O EditText utilizado como crédito.
     */
    private EditText createEditText(int id) {
        EditText editText = new EditText(this);
        editText.setId(id);
        editText.setHint(createHint(id));
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setKeyListener(decimalKeyListener);
        editText.addTextChangedListener(new CreditoTextWatcher());
        editText.setTextColor(getResources().getColor(R.color.credito_text));
        editText.setTextSize(25);
        
        editTextList.add(editText);
        
        return editText;
    }
    
    /**
     * Método que cria um Button para remoção de um crédito.
     * 
     * @return O Button utilizado para remoção de um crédito.
     */
    private Button createButton() {
        Button button = new Button(this);
        button.setText(R.string.remove_credito);
        button.setOnClickListener(btnRemoverCreditoListener);

        button.setBackgroundColor(getResources().getColor(R.color.light_gray));
        button.setTextColor(Color.WHITE);
        button.setTextSize(30);

        return button;
    }
    
    /**
     * Método que cria a hint utilizada em um crédito quando o mesmo não foi definido.
     * 
     * @param id O número referente à ordem do crédito.
     * @return A hint de um crédito.
     */
    private String createHint(int id) {
        return String.format(getResources().getString(R.string.hint), id);
    }
}
