import 'package:flutter/foundation.dart';
import '../../data/models/favorite_model.dart';
import '../../data/repositories/favorite_repository_impl.dart';

/// 收藏状态
enum FavoriteStatus {
  initial,
  loading,
  loaded,
  error,
}

/// 收藏 Provider
class FavoriteProvider extends ChangeNotifier {
  final FavoriteRepositoryImpl _favoriteRepository = FavoriteRepositoryImpl();

  FavoriteStatus _status = FavoriteStatus.initial;
  List<Favorite> _favorites = [];
  String? _errorMessage;
  final Set<String> _favoriteIds = {};

  FavoriteStatus get status => _status;
  List<Favorite> get favorites => _favorites;
  String? get errorMessage => _errorMessage;

  /// 是否已收藏
  bool isFavorite(String storyId) => _favoriteIds.contains(storyId);

  /// 加载收藏列表
  Future<void> loadFavorites({bool refresh = false}) async {
    if (refresh) {
      _favorites = [];
      _favoriteIds.clear();
    }

    _status = FavoriteStatus.loading;
    notifyListeners();

    try {
      _favorites = await _favoriteRepository.getFavorites();
      _favoriteIds.addAll(_favorites.map((f) => f.storyId));
      _status = FavoriteStatus.loaded;
    } catch (e) {
      _errorMessage = e.toString();
      _status = FavoriteStatus.error;
    }
    notifyListeners();
  }

  /// 切换收藏状态
  Future<void> toggleFavorite(String storyId) async {
    try {
      if (_favoriteIds.contains(storyId)) {
        await _favoriteRepository.removeFavorite(storyId);
        _favoriteIds.remove(storyId);
        _favorites.removeWhere((f) => f.storyId == storyId);
      } else {
        final favorite = await _favoriteRepository.addFavorite(storyId);
        _favoriteIds.add(storyId);
        _favorites.insert(0, favorite);
      }
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
      rethrow;
    }
  }

  /// 刷新收藏列表
  Future<void> refresh() async {
    await loadFavorites(refresh: true);
  }
}
