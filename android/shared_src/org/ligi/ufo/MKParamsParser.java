/**************************************************
 *                                             
 * class representing the Params Structure     
 *                                             
 * Author:        Marcus -LiGi- Bueschleb      
 * 
 * see README for further Infos
 *
 *************************************************/

package org.ligi.ufo;


public class MKParamsParser  extends ParamsClass
    implements MKParamsGeneratedDefinitions,org.ligi.ufo.DUBwiseLangDefs

{
	
	public final static byte INCOMPATIBLE_FLAG_NOT=0;
	public final static byte INCOMPATIBLE_FLAG_FC_TOO_OLD=1;
	public final static byte INCOMPATIBLE_FLAG_FC_TOO_NEW=2;

	// be positive that the params are not incompatible
	public byte incompatible_flag=INCOMPATIBLE_FLAG_NOT;
	
    public int[] poti_pos;
    public final static int MAX_PARAMSETS=5;
    //    public final static int MAX_PARAMLENGTH=100;

    public int[][] field;
    public int[][] field_bak;
    //    public String[] names={"","","","",""};

    
    public int[][] default_params = {
            {1,79,1,2,3,4,5,6,7,8,104,30,30,251,10,20,30,5,64,14,16,12,8,230,27,128,80,150,5,33,35,30,0,32,0,0,0,0,100,40,0,250,100,40,0,250,3,50,90,50,90,80,1,78,78,16,0,100,0,0,0,0,95,15,243,15,170,170,252,100,90,90,90,75,75,75,0,6,8,90,30,100,100,4,0,0,0,0,0,0,0,83,112,111,114,116,0,0,78,111,114,109,97},
            {2,79,1,2,3,4,5,6,7,8,104,30,30,251,10,15,30,5,64,10,16,6,8,230,27,128,90,120,5,33,35,30,0,32,0,0,0,0,100,40,0,250,100,40,0,250,3,50,90,50,90,80,60,78,78,32,0,75,0,0,0,0,95,20,243,20,170,170,252,100,90,90,90,75,75,75,0,6,8,90,30,100,100,4,0,0,0,0,0,0,0,78,111,114,109,97,108,0,0,66,101,103,105},
            {3,79,1,2,3,4,5,6,7,8,235,30,30,253,10,15,30,5,64,8,16,6,8,230,27,128,100,120,5,33,35,20,0,16,0,0,0,0,100,40,0,250,100,40,0,250,3,50,90,50,90,80,70,78,78,32,0,50,0,0,0,0,95,30,243,30,170,170,252,100,90,90,90,75,75,75,0,6,8,90,30,100,100,4,0,0,0,2,0,0,0,66,101,103,105,110,110,101,114,0,0,255,3},
            {4,79,1,2,3,4,5,6,7,8,104,30,30,251,10,15,30,5,64,10,16,6,8,230,27,128,90,120,5,33,35,30,0,32,0,0,0,0,100,40,0,250,100,40,0,250,3,50,90,50,90,80,60,78,78,32,0,75,0,0,0,0,95,20,243,20,170,170,252,100,90,90,90,75,75,75,0,6,8,90,30,100,100,4,0,0,0,0,0,0,0,78,111,114,109,97,108,0,0,66,101,103,105},
            {5,79,1,2,3,4,5,6,7,8,104,30,30,251,10,15,30,5,64,10,16,6,8,230,27,128,90,120,5,33,35,30,0,32,0,0,0,0,100,40,0,250,100,40,0,250,3,50,90,50,90,80,60,78,78,32,0,75,0,0,0,0,95,20,243,20,170,170,252,100,90,90,90,75,75,75,0,6,8,90,30,100,100,4,0,0,0,0,0,0,0,78,111,114,109,97,108,0,0,66,101,103,105}
    };
    
    public int act_paramset=0;

    public int params_version=-1;
    public int last_parsed_paramset=-1;
    public int active_paramset=-1;
    
    public boolean found_incompatible=false;

    public int[] stick_stringids;

    
    /** returns the field val for the act paramset at a given position **/
    public int get_field_from_act(int pos)
    {       return field[act_paramset][pos];    }

    /** set the field val for the act paramset at a given position **/
    public void set_field_from_act(int pos,int val)
    { 
    	// clip values
    	if (val>255) val=255;
    	if (val<0) val=0;
    	field[act_paramset][pos]=val;
    }


    /*    public void field_from_act_add(int pos,int val)
    {
	set_field_from_act(pos , get_field_from_act(pos)+val);
	}

    // for boolean Flags
    public void field_from_act_xor(int pos,int val)
    {
	field[act_paramset][pos]^=val;
    }

    */

    public void set_name(String nme)
    {
	if (nme.length()>10)
	    nme=nme.substring(0,10);
	//	names[act_paramset]=nme;
	int nme_pos=0;
	while(nme_pos<nme.length())
	    {
		field[act_paramset][name_start+nme_pos]=(byte)nme.charAt(nme_pos);
		nme_pos++;
	    }
	field[act_paramset][name_start+nme_pos]=0;
    }

    public MKParamsParser() 
    {
	field=new int[MAX_PARAMSETS][];
	field_bak=new int[MAX_PARAMSETS][];
	
	for (int ii=0;ii<MAX_PARAMSETS;ii++)
		{
		   field[ii]=null;
		   field_bak[ii]=null;
		  
		}

	stick_stringids=new int[MKStickData.MAX_STICKS];
	for (int i=0;i<MKStickData.MAX_STICKS;i++)
	    stick_stringids[i]=STRINGID_NOTREADYET;
	poti_pos=new int[8];
    }

    public  int length=0;
    public  int name_start=0;
    /*
    public int[] param_type;
    public int[] param_pos;
    public int[] param_innerpos;

    public  String[] tab_names;
    public  String[][] field_names;
    public  int[][] field_positions;
    public  int[][] field_types;

    public  int length;
    public  int name_start;
    */


    public void use_backup()
    {
    	set_by_mk_data(field_bak[act_paramset]);
    }

    public void update_backup(int to)
    {	
    	for ( int i=0 ; i<field[act_paramset].length;i++)
	    {
    		field_bak[to][i+2]=field[act_paramset][i];
    		field[to][i]=field[act_paramset][i];
	    }

    	field_bak[to][0]=to+1;
    	field_bak[to][1]=params_version;
    }

    public void reset()
    {
	last_parsed_paramset=-1;

	for (int ii=0;ii<MAX_PARAMSETS;ii++)
	    field[ii]=null;
    }
	
    public String getParamName(int paramset)
    {
	String res="";
	for ( int i=name_start;i<length;i++)
	    {
		if(field[paramset][i]==0)break;
		res+=(char)field[paramset][i];
	    }
	return res;
    }

    
    
    public void set_by_mk_data(int[] in_arr)
    {
	params_version=in_arr[1];
	int definition_pos=params_version-73;

	if ((definition_pos<0)||( (definition_pos>=all_tab_stringids.length))) 
	    {
		if ((definition_pos<0))
			incompatible_flag=INCOMPATIBLE_FLAG_FC_TOO_OLD;
		
		if( (definition_pos>=all_tab_stringids.length))
			incompatible_flag=INCOMPATIBLE_FLAG_FC_TOO_NEW;
		
		found_incompatible=true;
		return;
	    }

	last_parsed_paramset=in_arr[0]-1;

	if (active_paramset==-1)active_paramset=last_parsed_paramset;

	tab_stringids=all_tab_stringids[definition_pos];
	field_stringids=all_field_stringids[definition_pos];
	field_positions=all_field_positions[definition_pos];
	field_types=all_field_types[definition_pos];

	name_start=all_name_positions[definition_pos];
	length=all_lengths[definition_pos];

	field[last_parsed_paramset]=new int[length];
	field_bak[last_parsed_paramset]=new int[length+2];

	//	names[last_parsed_paramset]="";
	for ( int i=0;i<length+2;i++)
	    {
	    if (i<length) 
		field[last_parsed_paramset][i]=in_arr[i+2];
	    field_bak[last_parsed_paramset][i]=in_arr[i];
	    }


	for (int i=0;i<MKStickData.MAX_STICKS;i++)
	    stick_stringids[i]=STRINGID_NONE_ASSIGNED;

	for (int tab=0;tab<tab_stringids.length;tab++)
	    for (int item=0;item<field_types[tab].length;item++)
		if (field_types[tab][item]==PARAMTYPE_STICK)
		    {
			if ( field[last_parsed_paramset][field_positions[tab][item]]<12)
				stick_stringids[ field[last_parsed_paramset][field_positions[tab][item]] ] = field_stringids[tab][item];
			
		    switch(field_stringids[tab][item])
			{
			case STRINGID_POTI1:
			    poti_pos[0]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			case STRINGID_POTI2:
			    poti_pos[1]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			case STRINGID_POTI3:
			    poti_pos[2]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			case STRINGID_POTI4:
			    poti_pos[3]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			
			case STRINGID_POTI5:
			    poti_pos[4]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			    
			case STRINGID_POTI6:
			    poti_pos[5]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			    
			case STRINGID_POTI7:
			    poti_pos[6]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			
			case STRINGID_POTI8:
			    poti_pos[7]= field[last_parsed_paramset][field_positions[tab][item]] ;
			    break;
			
			}
		    }
		
		
    }

    public boolean allParamsetsKnown() {
    	boolean _tmp_fin=true;
		for(int i=0;i<MAX_PARAMSETS;i++)
		{
			_tmp_fin&=(field_bak[i]!=null);
		}
		return _tmp_fin;
    }


}
