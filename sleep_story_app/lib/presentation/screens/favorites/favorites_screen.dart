import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/favorite_provider.dart';

/// 收藏页面
class FavoritesScreen extends StatefulWidget {
  const FavoritesScreen({super.key});

  @override
  State<FavoritesScreen> createState() => _FavoritesScreenState();
}

class _FavoritesScreenState extends State<FavoritesScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<FavoriteProvider>().loadFavorites();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('我的收藏'),
      ),
      body: Consumer<FavoriteProvider>(
        builder: (context, provider, _) {
          if (provider.status == FavoriteStatus.loading && provider.favorites.isEmpty) {
            return const Center(child: CircularProgressIndicator());
          }

          if (provider.favorites.isEmpty) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.favorite_border,
                    size: 64,
                    color: Colors.grey[600],
                  ),
                  const SizedBox(height: 16),
                  Text(
                    '还没有收藏哦',
                    style: TextStyle(color: Colors.grey[600]),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '去发现更多好听的故事吧',
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),
            );
          }

          return RefreshIndicator(
            onRefresh: () => provider.refresh(),
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: provider.favorites.length,
              itemBuilder: (context, index) {
                final favorite = provider.favorites[index];
                return Padding(
                  padding: const EdgeInsets.only(bottom: 12),
                  child: _FavoriteStoryCard(
                    id: favorite.storyId,
                    title: favorite.storyTitle ?? '未知故事',
                    coverUrl: favorite.storyCoverUrl,
                    duration: favorite.storyDuration ?? 0,
                    onTap: () {
                      // TODO: 跳转到播放页面
                    },
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}

/// 收藏故事卡片
class _FavoriteStoryCard extends StatelessWidget {
  final String id;
  final String title;
  final String? coverUrl;
  final int duration;
  final VoidCallback? onTap;

  const _FavoriteStoryCard({
    required this.id,
    required this.title,
    this.coverUrl,
    required this.duration,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final minutes = duration ~/ 60;
    final seconds = duration % 60;
    final formattedDuration = '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';

    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          children: [
            // 封面图
            ClipRRect(
              borderRadius: const BorderRadius.horizontal(
                left: Radius.circular(12),
              ),
              child: coverUrl != null
                  ? Image.network(
                      coverUrl!,
                      width: 100,
                      height: 100,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(
                        width: 100,
                        height: 100,
                        color: AppColors.surfaceLight,
                        child: const Icon(Icons.music_note, color: AppColors.textSecondary),
                      ),
                    )
                  : Container(
                      width: 100,
                      height: 100,
                      color: AppColors.surfaceLight,
                      child: const Icon(Icons.music_note, color: AppColors.textSecondary),
                    ),
            ),
            // 内容
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: Theme.of(context).textTheme.titleMedium,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        const Icon(
                          Icons.access_time,
                          size: 14,
                          color: AppColors.textSecondary,
                        ),
                        const SizedBox(width: 4),
                        Text(
                          formattedDuration,
                          style: Theme.of(context).textTheme.bodySmall,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            IconButton(
              icon: const Icon(Icons.favorite, color: AppColors.error),
              onPressed: () {
                // 取消收藏
              },
            ),
          ],
        ),
      ),
    );
  }
}
