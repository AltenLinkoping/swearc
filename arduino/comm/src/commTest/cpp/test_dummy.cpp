#include "gtest/gtest.h"
#include "dummy.hpp"

using namespace testing;

TEST(CommTests, dummy_test) {
  ASSERT_TRUE(dummy_function(2));
  ASSERT_FALSE(dummy_function(1));
}

