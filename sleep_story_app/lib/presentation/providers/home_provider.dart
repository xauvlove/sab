import 'package:flutter/foundation.dart';
import '../../data/models/story_model.dart';
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
  List<Story> _featured = [];
  List<Category> _categories = [];
  List<Story> _recommended = [];
  String? _errorMessage;

  HomeStatus get status => _status;
  List<Story> get featured => _featured;
  List<Category> get categories => _categories;
  List<Story> get recommended => _recommended;
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
  Future<List<Story>> getStoriesByCategory(String categoryId) async {
    try {
      return await _storyRepository.getStoriesByCategory(categoryId);
    } catch (e) {
      rethrow;
    }
  }

  /// 搜索故事
  Future<List<Story>> searchStories(String keyword) async {
    try {
      return await _storyRepository.searchStories(keyword);
    } catch (e) {
      rethrow;
    }
  }
}
