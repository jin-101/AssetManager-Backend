package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.user.MultikeyForUserAsset;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Repository 
public interface UserAssetRepo extends CrudRepository<UserAssetDTO, MultikeyForUserAsset> { 
	
	@Query("select u from UserAssetDTO u where u.user=?1 and u.assetCode =?2 and u.detailCode=?3")
	List<UserAssetDTO> getSpecificUserAssets(UserDTO user,String assetCode,String detailCode);
	
	@Query("select u from UserAssetDTO u where u.user=?1 and u.assetCode =?2") 
	List<UserAssetDTO> getSpecificUserAssets(UserDTO user,String assetCode);   
	
	// 유저의 총자산 얻기 위한 메소드
	public List<UserAssetDTO> findByUser(UserDTO user); // ★★ 엔티티에 UserDTO user 로 매핑되어 있으므로 findByUser를 해야 에러 발생 X 
	                                                    // 비록 DB에는 user_id로 생성했지만, @Entity를 선언한 자바 객체에서는 user라는 변수를 쓰고 있으므로 이를 따라야 함          
 
}
