package com.narthil.rvcs.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.narthil.rvcs.dao.member.MemberDao;
import com.narthil.rvcs.dao.member.MemberRepository;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.MemberInfo;
import com.narthil.rvcs.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MemberServiceImpl
 */
@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberRepository memberRepository;

    // 添加成员
    public  ResultInfo<Map<String,Object>> addMember(MemberInfo member,String adminId) {
        ResultInfo<Map<String,Object>> orgResult=new ResultInfo<Map<String,Object>>();
        // 判断权限,以当前操作者的根组织和其id进行判断
        if(member!=null){
            int auth=memberDao.getAuthNum(member.getRootId(),adminId);
            System.out.println("操作者权限"+auth);
            if(auth!=1&&auth!=2){
                orgResult.setStatus(0,"权限不足");
                return orgResult;
            }
            // 判断待添加的用户是否已经存在在该公司
            if(memberDao.findByUserIdAndRootId(member.getRootId(),member.getUserId())==null){
                member.setAuth(0);
                member.setAuthDesc("默认权限");
                MemberInfo mTemp=memberRepository.save(member);
        
                Map<String,Object> dataMap=new HashMap<String,Object>(){
                    {
                        // put("id",mTemp.getId());
                        put("userId", mTemp.getUserId());
                        // put("rootId", mTemp.getRootId());
                        put("orgId", mTemp.getOrgId());
                        put("auth",mTemp.getAuth());
                        put("authDesc",mTemp.getAuthDesc());
                    }
                };
                orgResult.setData(dataMap);
                orgResult.setStatus(1,"添加成员成功");
            }else{
                orgResult.setStatus(0,"此成员已经在该公司中");
            }

        }else{
            orgResult.setStatus(0,"添加成员失败");
        }
        
        return orgResult;
    }

    // 修改成员权限
    public ResultInfo<Object> updateAuth(String rootId,String userId,String adminId,int auth){
        return null;
    }
}