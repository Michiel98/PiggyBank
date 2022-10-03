package com.piggebank.paybackPackage;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.piggebank.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.piggebank.paybackPackage.PayBackMain.getPaybacks;

public class tab2 extends Fragment {

    ListView resultsListView;
    HashMap<String,String> item;
    List<HashMap<String, String>> listItems;
    SimpleAdapter adapter;
    ArrayList<String> namesBorrowed;
    ArrayList<String> namesLoan;

    public tab2(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tab2, container, false);

        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultsListView = getView().findViewById(R.id.ListView2);

        namesBorrowed = new ArrayList<>();
        namesLoan = new ArrayList<>();

        item = new HashMap<>();

        laaditems();

        listItems = new ArrayList<>();
        adapter = new SimpleAdapter(getContext(),listItems, R.layout.list_item,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text1,R.id.text2});

        display();
    }

    public void laaditems(){
        item.clear();

        String newItem1 = null;
        String newItem2 = "";
        String newItem3 = null;
        String newItem4 = "";
        double item3 = 0;
        double item1 = 0;

        for(int i =0;i<getPaybacks().size();i++) {
            if (getPaybacks().get(i).getBorrowed()) {

                item1 = item1+getPaybacks().get(i).getPrice();
                if(!checkDubbelB(PayBackMain.getNickName(getPaybacks().get(i).getuidFriend()))){
                newItem2 = newItem2+" "+ PayBackMain.getNickName(getPaybacks().get(i).getuidFriend());
                }

            }else{
                item3 = item3+getPaybacks().get(i).getPrice();
                if(!checkDubbelL(PayBackMain.getNickName(getPaybacks().get(i).getuidFriend()))) {
                    newItem4 = newItem4 + " " + PayBackMain.getNickName(getPaybacks().get(i).getuidFriend());
                }
            }
        }

        newItem1 = "Total borrowed €"+item1;
        newItem3 = "Total loan €"+item3;

        newItem2 = "From :"+ newItem2;
        newItem4 = "To :"+newItem4;

        item.put(newItem1, newItem2);
        item.put(newItem3, newItem4);
    }

    public boolean checkDubbelB(String naam){
        boolean dubbel = false;
        for(int i=0; i<namesBorrowed.size();i++){

            if(namesBorrowed.get(i).equals(naam)){
                dubbel = true;
            }
        }
        if(!dubbel){
            namesBorrowed.add(naam);
        }

        return dubbel;
    }

    public boolean checkDubbelL(String naam){
        boolean dubbel = false;
        for(int i=0; i<namesLoan.size();i++){

            if(namesLoan.get(i).equals(naam)){
                dubbel = true;
            }

        }
        if(!dubbel){
            namesLoan.add(naam);
        }
        return dubbel;
    }

    public void display(){
        Iterator it = item.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String,String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line",pair.getKey().toString());
            resultsMap.put("Second Line",pair.getValue().toString());
            listItems.add(resultsMap);
        }

        resultsListView.setAdapter(adapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}