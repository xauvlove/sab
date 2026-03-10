import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/community_provider.dart';
import '../../providers/player_provider.dart';

/// 社区页面
class CommunityScreen extends StatefulWidget {
  const CommunityScreen({super.key});

  @override
  State<CommunityScreen> createState() => _CommunityScreenState();
}

class _CommunityScreenState extends State<CommunityScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<CommunityProvider>().loadFeed();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('社区创作'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add),
            onPressed: () => _showPublishDialog(context),
          ),
        ],
      ),
      body: Consumer<CommunityProvider>(
        builder: (context, provider, _) {
          if (provider.status == CommunityStatus.loading &&
              provider.stories.isEmpty) {
            return const Center(child: CircularProgressIndicator());
          }

          if (provider.stories.isEmpty) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.people_outline,
                    size: 64,
                    color: Colors.grey[600],
                  ),
                  const SizedBox(height: 16),
                  Text(
                    '暂无内容，快来发布第一个故事吧',
                    style: TextStyle(color: Colors.grey[600]),
                  ),
                  const SizedBox(height: 16),
                  ElevatedButton(
                    onPressed: () => _showPublishDialog(context),
                    child: const Text('发布故事'),
                  ),
                ],
              ),
            );
          }

          return RefreshIndicator(
            onRefresh: () => provider.refresh(),
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: provider.stories.length + (provider.hasMore ? 1 : 0),
              itemBuilder: (context, index) {
                if (index == provider.stories.length) {
                  // 加载更多
                  provider.loadMore();
                  return const Center(
                    child: Padding(
                      padding: EdgeInsets.all(16),
                      child: CircularProgressIndicator(),
                    ),
                  );
                }

                final story = provider.stories[index];
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // 用户信息
                        Row(
                          children: [
                            CircleAvatar(
                              radius: 16,
                              backgroundColor: AppColors.primary,
                              child: Text(
                                (story.nickname ?? '用户')[0],
                                style: const TextStyle(color: Colors.white),
                              ),
                            ),
                            const SizedBox(width: 8),
                            Text(
                              story.nickname ?? '匿名用户',
                              style: Theme.of(context).textTheme.titleSmall,
                            ),
                          ],
                        ),
                        const SizedBox(height: 12),
                        // 标题
                        Text(
                          story.title,
                          style: Theme.of(context).textTheme.titleMedium,
                        ),
                        const SizedBox(height: 8),
                        // 内容预览
                        Text(
                          story.content,
                          maxLines: 3,
                          overflow: TextOverflow.ellipsis,
                          style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                                color: AppColors.textSecondary,
                              ),
                        ),
                        const SizedBox(height: 12),
                        // 操作按钮
                        Row(
                          children: [
                            // 播放按钮
                            TextButton.icon(
                              icon: const Icon(Icons.play_circle_outline),
                              label: Text('播放 (${story.listenCount ?? 0})'),
                              onPressed: () {
                                // 使用 TTS 播放
                                context.read<PlayerProvider>().playText(
                                      story.id,
                                      '${story.title}。${story.content}',
                                    );
                              },
                            ),
                            const Spacer(),
                            // 点赞按钮
                            TextButton.icon(
                              icon: Icon(
                                story.isLiked
                                    ? Icons.favorite
                                    : Icons.favorite_border,
                                color: story.isLiked ? AppColors.error : null,
                              ),
                              label: Text('${story.likeCount ?? 0}'),
                              onPressed: () => provider.toggleLike(story.id),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }

  /// 显示发布对话框
  void _showPublishDialog(BuildContext context) {
    final titleController = TextEditingController();
    final contentController = TextEditingController();

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) => Padding(
        padding: EdgeInsets.only(
          bottom: MediaQuery.of(context).viewInsets.bottom,
        ),
        child: Container(
          padding: const EdgeInsets.all(16),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                '发布故事',
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 16),
              TextField(
                controller: titleController,
                decoration: const InputDecoration(
                  labelText: '标题',
                  hintText: '输入故事标题',
                ),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: contentController,
                maxLines: 5,
                decoration: const InputDecoration(
                  labelText: '内容',
                  hintText: '输入故事内容',
                  alignLabelWithHint: true,
                ),
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () async {
                  if (titleController.text.isEmpty ||
                      contentController.text.isEmpty) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('请填写完整信息')),
                    );
                    return;
                  }

                  try {
                    await context.read<CommunityProvider>().publishStory(
                          PublishStoryRequest(
                            title: titleController.text,
                            content: contentController.text,
                          ),
                        );
                    if (context.mounted) {
                      Navigator.pop(context);
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('发布成功')),
                      );
                    }
                  } catch (e) {
                    if (context.mounted) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('发布失败: $e')),
                      );
                    }
                  }
                },
                child: const Text('发布'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// 导入 PublishStoryRequest
import '../../../data/models/community_model.dart';
