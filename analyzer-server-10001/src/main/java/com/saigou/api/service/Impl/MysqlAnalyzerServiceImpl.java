package com.saigou.api.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saigou.api.service.IMysqlAnalyzerService;
import com.saigou.entity.*;
import com.saigou.util.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MysqlAnalyzerServiceImpl implements IMysqlAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(MysqlAnalyzerServiceImpl.class);
    private final FaceBoxServiceImpl faceBoxService;
    private final PersonBoxServiceImpl personBoxService;

    @Override
    public void addAnalysisResult(Long controlId,AnalysisResult result) {
        Long timestamp = result.getTimestamp();
        List<FaceBox> faceBoxes = result.getFaceBoxes();
        List<PersonBox> personBoxes = result.getPersonBoxes();
        List<ControlFace> collect = faceBoxes.stream().map(faceBox -> Utils.FaceBox2ControlFace(controlId, timestamp, faceBox)).toList();
        List<ControlPerson> collect1 = personBoxes.stream().map(personBox -> Utils.PersonBox2ControlPerson(controlId, timestamp, personBox)).toList();
        faceBoxService.saveBatch(collect);
        personBoxService.saveBatch(collect1);
        log.info("addAnalysisResult完成");
    }

    @Override
    @Transactional
    public void removeAnalysisResult(Long controlId, Long timestamp) {
        faceBoxService.removeBatchByIds(faceBoxService.list(new QueryWrapper<ControlFace>().eq("control_id", controlId).eq("timestamp", timestamp)));
        personBoxService.removeBatchByIds(personBoxService.list(new QueryWrapper<ControlPerson>().eq("control_id", controlId).eq("timestamp", timestamp)));
    }

    @Override
    @Transactional
    public void removeAnalysisResult(Long controlId) {
        faceBoxService.removeBatchByIds(faceBoxService.list(new QueryWrapper<ControlFace>().eq("control_id", controlId)));
        personBoxService.removeBatchByIds(personBoxService.list(new QueryWrapper<ControlPerson>().eq("control_id", controlId)));

    }

    @Override
    public AnalysisResult getAnalysisResult(Long controlId, Long timestamp) {
        List<ControlFace> ls = faceBoxService.list(new QueryWrapper<ControlFace>().eq("control_id", controlId))
                .stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).toList();
        List<ControlPerson> ls2 = personBoxService.list(new QueryWrapper<ControlPerson>().eq("control_id", controlId))
                .stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).toList();
        if(!ls.isEmpty() || !ls2.isEmpty()){
            Set<Long> timestamps = ls.stream().map(ControlFace::getTimestamp).collect(Collectors.toSet());
            timestamps.addAll(ls2.stream().map(ControlPerson::getTimestamp).collect(Collectors.toSet()));
            AnalysisResult result = new AnalysisResult();
            result.setTimestamp(timestamp);
            result.setFaceBoxes(ls.stream().map(Utils::ControlFace2FaceBox).collect(Collectors.toList()));
            result.setPersonBoxes(ls2.stream().map(Utils::ControlPerson2PersonBox).collect(Collectors.toList()));
            return result;
        }
        return null;
    }

    @Override
    public ControlAnalyzerResult getAnalysisResult(Long controlId, Long start_timestamp, Long end_timestamp) {
        List<ControlFace> ls = faceBoxService.list(new QueryWrapper<ControlFace>().eq("control_id", controlId))
                .stream().filter(l -> l.getTimestamp() >= start_timestamp && l.getTimestamp() <= end_timestamp).toList();
        List<ControlPerson> ls2 = personBoxService.list(new QueryWrapper<ControlPerson>().eq("control_id", controlId))
                .stream().filter(l -> l.getTimestamp() >= start_timestamp && l.getTimestamp() <= end_timestamp).toList();
        if(!ls.isEmpty() || !ls2.isEmpty()){
            Set<Long> timestamps = ls.stream().map(ControlFace::getTimestamp).collect(Collectors.toSet());
            timestamps.addAll(ls2.stream().map(ControlPerson::getTimestamp).collect(Collectors.toSet()));
            List<AnalysisResult> results = new ArrayList<>();
            timestamps.forEach(timestamp -> {
                AnalysisResult result = new AnalysisResult();
                result.setTimestamp(timestamp);
                result.setFaceBoxes(ls.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).
                        map(Utils::ControlFace2FaceBox).collect(Collectors.toList()));
                result.setPersonBoxes(ls2.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).
                        map(Utils::ControlPerson2PersonBox).collect(Collectors.toList()));
                results.add(result);
            });
            results.sort(Comparator.comparing(AnalysisResult::getTimestamp));
            return new ControlAnalyzerResult(controlId, results);
        }
        return null;
    }

    @Override
    public ControlAnalyzerResult getAnalysisResult(Long controlId) {
        List<ControlFace> ls = faceBoxService.list(new QueryWrapper<ControlFace>().eq("control_id", controlId));
        List<ControlPerson> ls2 = personBoxService.list(new QueryWrapper<ControlPerson>().eq("control_id", controlId));
        if(!ls.isEmpty() || !ls2.isEmpty()){
            Set<Long> timestamps = ls.stream().map(ControlFace::getTimestamp).collect(Collectors.toSet());
            timestamps.addAll(ls2.stream().map(ControlPerson::getTimestamp).collect(Collectors.toSet()));
            List<AnalysisResult> results = new ArrayList<>();
            timestamps.forEach(timestamp -> {
                AnalysisResult result = new AnalysisResult();
                result.setTimestamp(timestamp);
                result.setFaceBoxes(ls.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).
                        map(Utils::ControlFace2FaceBox).collect(Collectors.toList()));
                result.setPersonBoxes(ls2.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).
                        map(Utils::ControlPerson2PersonBox).collect(Collectors.toList()));
                results.add(result);
            });
            results.sort(Comparator.comparing(AnalysisResult::getTimestamp));
            return new ControlAnalyzerResult(controlId, results);
        }
        return null;
    }

    @Override
    public List<ControlAnalyzerResult> getAnalysisResult() {
        List<ControlFace> ls = faceBoxService.list();
        List<ControlPerson> ls2 = personBoxService.list();
        List<ControlAnalyzerResult> all = new ArrayList<>();
        if(!ls.isEmpty() || !ls2.isEmpty()){
            Set<Long> controlId = ls.stream().map(ControlFace::getControlId).collect(Collectors.toSet());
            controlId.addAll(ls2.stream().map(ControlPerson::getControlId).collect(Collectors.toSet()));
            controlId.forEach(id -> {
                all.add(new ControlAnalyzerResult(id, null));
            });
            all.forEach(result -> {
                List<AnalysisResult> results = new ArrayList<>();
                List<ControlFace> list = ls.stream().filter(l -> Objects.equals(l.getControlId(), result.getControlId())).toList();
                List<ControlPerson> list2 = ls2.stream().filter(l -> Objects.equals(l.getControlId(), result.getControlId())).toList();
                Set<Long> timestampskeys = list.stream().map(ControlFace::getTimestamp).collect(Collectors.toSet());
                timestampskeys.addAll(list2.stream().map(ControlPerson::getTimestamp).collect(Collectors.toSet()));
                timestampskeys.forEach(
                        timestamp -> {
                            AnalysisResult o = new AnalysisResult();
                            o.setTimestamp(timestamp);
                            o.setPersonBoxes(list2.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).map(Utils::ControlPerson2PersonBox).toList());
                            o.setFaceBoxes(list.stream().filter(l -> Objects.equals(l.getTimestamp(), timestamp)).map(Utils::ControlFace2FaceBox).toList());
                            results.add(o);
                        }
                );
                results.sort(Comparator.comparing(AnalysisResult::getTimestamp));
                result.setResult(results);
            });
            return all;
        }
        return null;
    }
}
