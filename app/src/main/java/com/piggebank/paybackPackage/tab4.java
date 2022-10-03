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

public class tab4 extends Fragment {
    ListView resultsListView;
    HashMap<String,String> loan;
    List<HashMap<String, String>> listItems;
    SimpleAdapter adapter1;

    public tab4(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tab4, container, false);


        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultsListView = getView().findViewById(R.id.ListView4);
        loan = new HashMap<>();

        laaditems();

        listItems = new ArrayList<>();
        adapter1 = new SimpleAdapter(getContext(),listItems, R.layout.list_item,
                new String[]{"First Line","Second Line"},
                new int[]{R.id.text1,R.id.text2});

        display();
    }

    public void laaditems(){
        loan.clear();
        for(int i =0;i<PayBackMain.getPaybacks().size();i++) {
            if (!PayBackMain.getPaybacks().get(i).getBorrowed()) {

                String newItem1 = PayBackMain.getNickName(PayBackMain.getPaybacks().get(i).getuidFriend());
                String newItem2 = "On " + PayBackMain.getPaybacks().get(i).getDate() + " Amount: €" + PayBackMain.getPaybacks().get(i).getPrice();

                if(checkExist(newItem1)){ // indien payback reeds aanwezig, update payback
                    String value = loan.get(newItem1);

                    String substr = value.substring(value.length() - 4);

                    double firstValue = Double.parseDouble(substr);

                    double difference = firstValue+ PayBackMain.getPaybacks().get(i).getPrice();

                    String newItem3 = "Last payback date: " + PayBackMain.getPaybacks().get(i).getDate() + "   Amount: €" + difference;

                    loan.put(newItem1,newItem3);

                } else {
                    loan.put(newItem1, newItem2);
                }
            }
        }
    }

    public boolean checkExist(String nickname){
        boolean c = false;
        for ( String key : loan.keySet() ) {
            if(key.equals(nickname)){
                c=true;
            }
        }
        return c;
    }

    public void display(){
        Iterator it = loan.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String,String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line",pair.getKey().toString());
            resultsMap.put("Second Line",pair.getValue().toString());
            listItems.add(resultsMap);
        }

        resultsListView.setAdapter(adapter1);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
