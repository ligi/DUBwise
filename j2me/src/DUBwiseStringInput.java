import javax.microedition.lcdui.*;


public class DUBwiseStringInput
    implements CommandListener,Runnable
{
    public Form input_form;
    private TextField str_field;
    private Command ok_cmd;
    private Command cancel_cmd;

    public DUBwiseCanvas canvas;

    boolean input_done=false;
    byte input_id;

    public DUBwiseStringInput(String name,int maxlength,int input_id,DUBwiseCanvas _canvas)
    {
	canvas=_canvas;
	input_form=new Form("String Input");
	ok_cmd = new Command("OK", Command.OK, 1);
	cancel_cmd = new Command("Cancel", Command.BACK, 2);

	str_field=new TextField(name, "", maxlength, TextField.ANY);

	input_form.append(str_field);
	input_form.addCommand(ok_cmd);
	input_form.addCommand(cancel_cmd);
	input_form.setCommandListener(this);

	canvas.root.display.setCurrent(input_form);	
	new Thread(this).start();

    }

    public void run()
    {
	while (!input_done)
	    try {
	    Thread.sleep(10);
	    }
	    catch (Exception e) { }

	canvas.root.display.setCurrent(canvas);
    }

    public void commandAction( Command cmd, Displayable dis) 
    {
	if(cmd!=cancel_cmd)
	    canvas.string_input_result(input_id,str_field.getString());
	    
	input_done=true;
    }
			    

}