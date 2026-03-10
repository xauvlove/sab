import 'package:flutter/foundation.dart';
import '../../data/models/community_model.dart';
import '../../data/repositories/community_repository_impl.dart';

/// 社区状态
enum CommunityStatus {
  initial,
  loading,
  loaded,
  error,
}

/// 社区 Provider
class CommunityProvider extends ChangeNotifier {
  final CommunityRepositoryImpl _communityRepository = CommunityRepositoryImpl();

  CommunityStatus _status = CommunityStatus.initial;
  List<CommunityStory> _stories = [];
  String? _errorMessage;
  int _currentPage = 1;
  bool _hasMore = true;

  CommunityStatus get status => _status;
  List<CommunityStory> get stories => _stories;
  String? get errorMessage => _errorMessage;
  bool get hasMore => _hasMore;

  /// 加载社区动态
  Future<void> loadFeed({bool refresh = false}) async {
    if (refresh) {
      _currentPage = 1;
      _hasMore = true;
    }

    if (!_hasMore && !refresh) return;

    _status = CommunityStatus.loading;
    if (refresh) _errorMessage = null;
    notifyListeners();

    try {
      final newStories = await _communityRepository.getFeed(
        page: _currentPage,
        size: 20,
      );

      if (refresh) {
        _stories = newStories;
      } else {
        _stories.addAll(newStories);
      }

      _hasMore = newStories.length >= 20;
      _currentPage++;
      _status = CommunityStatus.loaded;
    } catch (e) {
      _errorMessage = e.toString();
      _status = CommunityStatus.error;
    }
    notifyListeners();
  }

  /// 加载更多
  Future<void> loadMore() async {
    if (_hasMore) {
      await loadFeed();
    }
  }

  /// 刷新
  Future<void> refresh() async {
    await loadFeed(refresh: true);
  }

  /// 点赞/取消点赞
  Future<void> toggleLike(String storyId) async {
    try {
      await _communityRepository.toggleLike(storyId);
      // 更新本地状态
      final index = _stories.indexWhere((s) => s.id == storyId);
      if (index != -1) {
        final story = _stories[index];
        _stories[index] = story.copyWith(
          isLiked: !story.isLiked,
          likeCount: (story.likeCount ?? 0) + (story.isLiked ? -1 : 1),
        );
        notifyListeners();
      }
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
      rethrow;
    }
  }

  /// 发布故事
  Future<CommunityStory> publishStory(PublishStoryRequest request) async {
    try {
      final story = await _communityRepository.publishStory(request);
      _stories.insert(0, story);
      notifyListeners();
      return story;
    } catch (e) {
      rethrow;
    }
  }

  /// 删除故事
  Future<void> deleteStory(String storyId) async {
    try {
      await _communityRepository.deleteStory(storyId);
      _stories.removeWhere((s) => s.id == storyId);
      notifyListeners();
    } catch (e) {
      rethrow;
    }
  }
}
