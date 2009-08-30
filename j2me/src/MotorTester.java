public class MotorTester
    extends org.ligi.ufo.ParamsClass
{
    public final static int MAX_ENGINES=12;

    int[] engine_field;
    int[] engine_field_bak;

    public void backup_engine_field()
    {
	for (int i=0;i<MAX_ENGINES;i++)
	    engine_field_bak[i]=engine_field[i];
    }


    public void use_backup()
    {
	for (int i=0;i<MAX_ENGINES;i++)
	    engine_field[i]=engine_field_bak[i];
    }
    
    
    public boolean new_is_dangerous()
	{
	    for (int i=0;i<MAX_ENGINES;i++)
		if ((engine_field[i]>99)&&(engine_field_bak[i]<100))
		    return true;

	    return false;
	    
	}

    public void set_field_from_act(int pos,int val)
    {
	engine_field[pos]=val;
    }

    public int get_field_from_act(int pos)
    {
	return engine_field[pos];
    }

    public MotorTester()
    {

	engine_field_bak=new int[MAX_ENGINES];
	engine_field=new int[MAX_ENGINES];
	tab_stringids=null; // no tabs

	field_strings=new String[1][MAX_ENGINES];
	field_positions=new int[1][MAX_ENGINES];
	field_types=new int[1][MAX_ENGINES];

	for (int i=0;i<MAX_ENGINES;i++)
	    {
		field_strings[0][i]="Engine " + (i+1);
		field_positions[0][i]=i;
		field_types[0][i]=PARAMTYPE_BYTE;
	    }

    }
}
