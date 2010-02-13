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



public abstract class ParamsClass
    implements MKParamsGeneratedDefinitions
{

    public int[]   tab_stringids;

    public int[][] field_stringids;
    public String[][] field_strings;

    public int[][] choice_stringids;


    //    public  String[] tab_names;
    //-    public  String[][] field_names;
    public  int[][] field_positions;
    public  int[][] field_types;
    //- public  String[][] choice_strings;

    abstract public int get_field_from_act(int pos);


    abstract public void set_field_from_act(int pos,int val);


    public void field_from_act_add_min_max(int pos,int val,int min,int max)
    {
	if (((get_field_from_act(pos)+val)>=min)&&((get_field_from_act(pos)+val)<=max))
	set_field_from_act(pos , get_field_from_act(pos)+val);
    }

    public void field_from_act_add(int pos,int val)
    {
	set_field_from_act(pos , get_field_from_act(pos)+val);
    }

    public void field_from_act_add_mod(int pos,int val,int mod)
    {
	int res=(get_field_from_act(pos)+val)%mod;
	if ( (res)<0) res=mod-1;
	    
	set_field_from_act(pos , res);
    }

    public void field_from_act_xor(int pos,int val)
    {
	set_field_from_act(pos , get_field_from_act(pos)^val);
    }

}
