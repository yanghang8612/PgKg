package com.casc.pgkg.adapter;

import com.casc.pgkg.MyVars;
import com.casc.pgkg.R;
import com.casc.pgkg.bean.Bucket;
import com.casc.pgkg.utils.CommonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BucketAdapter extends BaseQuickAdapter<Bucket, BaseViewHolder> {

    public BucketAdapter(List<Bucket> data) {
        super(R.layout.item_bucket, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bucket item) {
        helper.setText(R.id.tv_bucket_product_name, CommonUtils.getProduct(item.getEPC()).getStr())
                .setText(R.id.tv_bucket_body_code, CommonUtils.getBodyCode(item.getEPC()));
    }
}
