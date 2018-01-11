package com.whycools.transport.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.whycools.transport.R;
import com.whycools.transport.adapter.FinishShipListAdapter;
import com.whycools.transport.base.BaseFragment;
import com.whycools.transport.entity.ShipList;
import com.whycools.transport.service.ShipListService;

import java.util.ArrayList;
import java.util.List;

/**
 * 已经完成的清单
 * Created by Zero on 2016-12-06.
 */

public class FinishShipListFragment extends BaseFragment {
    private static final String TAG="已经完成的清单";
    private ShipListService shiplistService;//shiplist数据库
    private ListView finishshiplist_lv;
    private FinishShipListAdapter adapter;
    private List<ShipList> list=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_finishshiplist,container,false);
        init(view);
        return view;
    }
    private void init(View view){
        finishshiplist_lv= view.findViewById(R.id.finishshiplist_lv);

    }

    @Override
    protected void lazyLoad() {
        shiplistService=new ShipListService(getContext());
        list=shiplistService.getshiplistfinish();
        adapter=new FinishShipListAdapter(getContext(),list);
        finishshiplist_lv.setAdapter(adapter);

    }
}
