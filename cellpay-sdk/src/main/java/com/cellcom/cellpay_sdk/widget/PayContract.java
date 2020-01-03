package com.cellcom.cellpay_sdk.widget;

import com.cellcom.cellpay_sdk.api.Config;

public interface PayContract {
    interface View {

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();

        void setPresenter(Presenter presenter);
    }

    interface Presenter {

        String checkConfig(Config config);

        void setCustomButtonView();

        void setButtonStyle(int id);

        void setButtonText(String text);

        void setButtonClick();

        void openForm();

        void destroyCheckOut();
    }
}
