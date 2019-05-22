package com.casc.pgkg.helper.net;

import com.casc.pgkg.helper.net.param.Reply;

public class EmptyAdapter extends NetAdapter {

    @Override
    public void onSuccess(Reply reply) {}

    @Override
    public void onFail(String msg) {}
}
