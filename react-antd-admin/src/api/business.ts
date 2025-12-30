import type { PageData } from '@/interface';
import type { BuniesssUser } from '@/interface/business';

import { request } from './request';

export const getBusinessUserList = (params: any) => request<PageData<BuniesssUser>>('get', '/api/v3/seller', params);
