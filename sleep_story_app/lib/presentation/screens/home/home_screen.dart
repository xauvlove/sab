import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/home_provider.dart';
import '../../widgets/story_card.dart';

/// 首页
class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<HomeProvider>().loadHomeData();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Consumer<HomeProvider>(
          builder: (context, homeProvider, _) {
            if (homeProvider.status == HomeStatus.loading &&
                homeProvider.categories.isEmpty) {
              return const Center(child: CircularProgressIndicator());
            }

            if (homeProvider.status == HomeStatus.error) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.error_outline, size: 48, color: AppColors.error),
                    const SizedBox(height: 16),
                    Text(homeProvider.errorMessage ?? '加载失败'),
                    const SizedBox(height: 16),
                    ElevatedButton(
                      onPressed: () => homeProvider.loadHomeData(),
                      child: const Text('重试'),
                    ),
                  ],
                ),
              );
            }

            return RefreshIndicator(
              onRefresh: () => homeProvider.refresh(),
              child: CustomScrollView(
                slivers: [
                  // 顶部标题
                  SliverToBoxAdapter(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Row(
                        children: [
                          const Icon(
                            Icons.nightlight_round,
                            color: AppColors.primary,
                            size: 28,
                          ),
                          const SizedBox(width: 8),
                          Text(
                            '睡眠故事',
                            style: Theme.of(context).textTheme.headlineSmall,
                          ),
                        ],
                      ),
                    ),
                  ),

                  // 分类导航
                  SliverToBoxAdapter(
                    child: SizedBox(
                      height: 50,
                      child: ListView.builder(
                        scrollDirection: Axis.horizontal,
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        itemCount: homeProvider.categories.length,
                        itemBuilder: (context, index) {
                          final category = homeProvider.categories[index];
                          return Padding(
                            padding: const EdgeInsets.only(right: 8),
                            child: ActionChip(
                              label: Text(category.name),
                              backgroundColor: AppColors.surface,
                              labelStyle: const TextStyle(color: AppColors.textPrimary),
                              onPressed: () {
                                // TODO: 跳转到分类页面
                              },
                            ),
                          );
                        },
                      ),
                    ),
                  ),

                  // 推荐故事标题
                  const SliverToBoxAdapter(
                    child: Padding(
                      padding: EdgeInsets.all(16),
                      child: Text(
                        '为你推荐',
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),

                  // 推荐故事列表
                  SliverPadding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    sliver: SliverList(
                      delegate: SliverChildBuilderDelegate(
                        (context, index) {
                          final story = homeProvider.recommended[index];
                          return Padding(
                            padding: const EdgeInsets.only(bottom: 12),
                            child: StoryCard(
                              story: story,
                              onTap: () {
                                // TODO: 跳转到播放页面
                              },
                            ),
                          );
                        },
                        childCount: homeProvider.recommended.length,
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
