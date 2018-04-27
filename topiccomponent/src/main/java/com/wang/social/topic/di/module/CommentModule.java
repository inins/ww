package com.wang.social.topic.di.module;

import com.frame.di.scope.ActivityScope;
import com.wang.social.topic.mvp.contract.CommentContract;
import com.wang.social.topic.mvp.model.CommentModel;

import dagger.Module;
import dagger.Provides;

@Module
public class CommentModule {

    private CommentContract.View view;

    public CommentModule(CommentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CommentContract.View provideCommentView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CommentContract.Model provideCommentModel(CommentModel model) {
        return model;
    }
}
