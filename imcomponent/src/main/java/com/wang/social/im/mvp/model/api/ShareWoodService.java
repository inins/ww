package com.wang.social.im.mvp.model.api;

import com.frame.component.entities.ShareUserInfo;
import com.frame.component.entities.dto.ShareUserInfoDTO;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.wang.social.im.mvp.model.entities.dto.ShareDTO;
import com.wang.social.im.mvp.model.entities.dto.ShareInfoDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:43
 * ============================================
 */
public interface ShareWoodService {

    /**
     * 获取分享树数据
     *
     * @param version
     * @param type
     * @param objectId
     * @return
     */
    @GET("app/share/tree")
    Observable<BaseJson<ShareDTO>> getTreeData(@Query("v") String version, @Query("objectType") String type, @Query("objectId") String objectId);

    /**
     * 分享统计
     *
     * @param version
     * @param type
     * @param id
     * @return
     */
    @GET("app/share/overall")
    Observable<BaseJson<ShareInfoDTO>> getShareInfo(@Query("v") String version, @Query("objectType") String type, @Query("objectId") String id);

    /**
     * 分享榜单
     *
     * @param version
     * @param shareType
     * @param id
     * @return
     */
    @GET("app/share/overall_list")
    Observable<BaseJson<PageListDTO<ShareUserInfoDTO, ShareUserInfo>>> getShareList(@Query("v") String version, @Query("objectType") String shareType,
                                                                                    @Query("objectId") String id, @Query("current") int currentPage,
                                                                                    @Query("size") int pageSize);
}
