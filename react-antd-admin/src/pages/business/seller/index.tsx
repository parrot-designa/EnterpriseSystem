import type { MyPageTableOptions } from '@/components/business/page';
import type { BuniesssUser } from '@/interface/business';
import type { FC } from 'react';

import { Space, Tag } from 'antd';

import { getBusinessUserList } from '@/api/business';
import MyButton from '@/components/basic/button';
import MyPage from '@/components/business/page';

const { Item: SearchItem } = MyPage.MySearch;

const tableColums: MyPageTableOptions<BuniesssUser> = [
  { title: '卖家名称', dataIndex: 'name', key: 'name' },
  { title: '卖家代码', dataIndex: 'code', key: 'code' },
];

const BusinessWithSearchPage: FC = () => {
  return (
    <MyPage
      pageApi={getBusinessUserList}
      searchRender={
        <>
          <SearchItem label="卖家名称" name="name" type="input" />
        </>
      }
      tableOptions={tableColums}
    ></MyPage>
  );
};

export default BusinessWithSearchPage;
