import '../../data/models/favorite_model.dart';

/// 收藏仓储接口
abstract class FavoriteRepository {
  /// 获取收藏列表
  Future<List<Favorite>> getFavorites({int page = 1, int size = 20});

  /// 添加收藏
  Future<Favorite> addFavorite(String storyId);

  /// 取消收藏
  Future<void> removeFavorite(String storyId);

  /// 检查是否已收藏
  Future<bool> isFavorite(String storyId);
}
