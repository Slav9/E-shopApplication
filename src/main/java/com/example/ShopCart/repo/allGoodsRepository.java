// repository for Tovar model (named this way because of future updates)
package com.example.ShopCart.repo;

import com.example.ShopCart.models.tovar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface allGoodsRepository extends JpaRepository<tovar,Long> {
}
