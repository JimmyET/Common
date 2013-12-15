package com.easyhome.common.share.option;

import android.app.Activity;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.share.IShareObject;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.tencent.tauth.Tencent;

/**
 * QQ好友
 *
 * @author zhoulu
 * @date 13-12-13
 */
public class QQFriends extends QQConnect {

    private int mExtarFlag;

    @Override
    public int getIcon() {
        return R.drawable.bt_share_qq;
    }

    @Override
    public String getName() {
        return getString(R.string.action_qqfriends);
    }

    @Override
    public void onShare(IShareObject... shareObject) {

        if (shareObject == null || shareObject.length == 0) {
            return;
        }

        IShareObject object = shareObject[0];

        final Bundle params = new Bundle();
        int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
        switch (object.getType()) {
            case TYPE_IMAGE://只支持本地
                shareType = Tencent.SHARE_TO_QQ_TYPE_IMAGE;
                params.putString(Tencent.SHARE_TO_QQ_TITLE, object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, object.getRedirectUrl());
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, object.getSecondTitle());
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL, object.getThumbnailUrl()[0]);
                break;
            case TYPE_TEXT:
                params.putString(Tencent.SHARE_TO_QQ_TITLE, object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, object.getRedirectUrl());
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, object.getSecondTitle());
                break;
            case TYPE_WEBURL:
                params.putString(Tencent.SHARE_TO_QQ_TITLE, object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, object.getRedirectUrl());
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, object.getSecondTitle());
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, object.getThumbnailUrl()[0]);
                break;
            case TYPE_MUSIC:
            case TYPE_VIDEO:
                shareType = Tencent.SHARE_TO_QQ_TYPE_AUDIO;
                params.putString(Tencent.SHARE_TO_QQ_TITLE, object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, object.getRedirectUrl());
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, object.getSecondTitle());
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, object.getThumbnailUrl()[0]);
                params.putString(Tencent.SHARE_TO_QQ_AUDIO_URL, object.getMediaUrl());
                break;
        }

        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, ShareConfiguration.APPNAME);
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);

        if (!ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM) {
            if (ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG) {
                // 最后一个二进制位置为1, 其他位不变
                mExtarFlag |= Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
            } else {
                // 最后一个二进制位置为0, 其他位不变
                mExtarFlag &= (0xFFFFFFFF - Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }
        } else if (!ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG) {
            if (ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM) {
                // 倒数第二位置为1, 其他位不变
                mExtarFlag |= Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
            } else {
                // 倒数第二位置为0, 其他位不变
                mExtarFlag &= (0xFFFFFFFF - Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            }
        }
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, mExtarFlag);

        mTencent.shareToQQ((Activity) getContext(), params, this);
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {
        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        for (IShareObject object : shareObjects) {
            if (object == null) {
                notifyEvent(getString(R.string.share_invalidate_datas));
                return false;
            }

            if (TextUtil.isEmpty(object.getTitle())) {
                notifyEvent(getString(R.string.share_text_empty));
                return false;
            } else if(TextUtil.isEmpty(object.getSecondTitle())) {
                notifyEvent(getString(R.string.share_text_title_empty));
                return false;
            } else if(TextUtil.isEmpty(object.getRedirectUrl())
                    || !URIUtil.isValidHttpUri(object.getRedirectUrl())) {
                notifyEvent(getString(R.string.share_invalidate_redirect_url));
                return false;
            }

            IShareObject.TYPE type = object.getType();

            switch (type) {
                case TYPE_WEBURL:
                    String[] urls = object.getThumbnailUrl();
                    if (urls == null || urls.length == 0 || TextUtil.isEmpty(urls[0])
                            || !URIUtil.isValidHttpUri(urls[0])) {
                        notifyEvent(getString(R.string.share_webpage_invalidate_url));
                        return false;
                    }
                    break;
                case TYPE_IMAGE:
                    String[] local = object.getThumbnailUrl();
                    if (local == null || local.length == 0 || TextUtil.isEmpty(local[0])
                            || !URIUtil.isValidSdcardUri(local[0])) {
                        notifyEvent(getString(R.string.share_image_only_for_local));
                        return false;
                    }
                    break;
                case TYPE_MUSIC:
                case TYPE_VIDEO:
                    String[] online = object.getThumbnailUrl();
                    if (online == null || online.length == 0 || TextUtil.isEmpty(online[0])
                            || !URIUtil.isValidHttpUri(online[0])) {
                        notifyEvent(getString(R.string.share_image_invalid_url));
                        return false;
                    } else if(TextUtil.isEmpty(object.getMediaUrl())
                            || !URIUtil.isValidHttpUri(object.getMediaUrl())) {
                        notifyEvent(type == IShareObject.TYPE.TYPE_IMAGE ?
                                getString(R.string.share_music_invalidate_url)
                                :
                                getString(R.string.share_video_invalidate_url));
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}
