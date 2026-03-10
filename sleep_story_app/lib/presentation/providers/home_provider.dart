import 'package:flutter/foundation.dart';
import '../../data/models/story_model.dart' as models;
import '../../data/repositories/story_repository_impl.dart';

/// 首页状态
enum HomeStatus {
  initial,
  loading,
  loaded,
  error,
}

/// 首页 Provider
class HomeProvider extends ChangeNotifier {
  final StoryRepositoryImpl _storyRepository = StoryRepositoryImpl();

  HomeStatus _status = HomeStatus.initial;
  List<models.Story> _featured = [];
  List<models.Category> _categories = [];
  List<models.Story> _recommended = [];
  String? _errorMessage;

  HomeStatus get status => _status;
  List<models.Story> get featured => _featured;
  List<models.Category> get categories => _categories;
  List<models.Story> get recommended => _recommended;
  String? get errorMessage => _errorMessage;

  /// 加载首页数据
  Future<void> loadHomeData() async {
    _status = HomeStatus.loading;
    _errorMessage = null;
    notifyListeners();

    try {
      final data = await _storyRepository.getHomeData();
      _featured = data.featured;
      _categories = data.categories;
      _recommended = data.recommended;
      _status = HomeStatus.loaded;
    } catch (e) {
      _errorMessage = e.toString();
      _status = HomeStatus.error;
    }
    notifyListeners();
  }

  /// 刷新数据
  Future<void> refresh() async {
    await loadHomeData();
  }

  /// 根据分类获取故事
  Future<List<models.Story>> getStoriesByCategory(String categoryId) async {
    try {
      return await _storyRepository.getStoriesByCategory(categoryId);
    } catch (e) {
      rethrow;
    }
  }

  /// 搜索故事
  Future<List<models.Story>> searchStories(String keyword) async {
    try {
      return await _storyRepository.searchStories(keyword);
    } catch (e) {
      rethrow;
    }
  }
}
